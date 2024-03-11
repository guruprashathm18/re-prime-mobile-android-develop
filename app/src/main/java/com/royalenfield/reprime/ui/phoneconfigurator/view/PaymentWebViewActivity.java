package com.royalenfield.reprime.ui.phoneconfigurator.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.network.listener.INetworkStateListener;
import com.royalenfield.reprime.network.receiver.NetworkConnectionReceiver;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.phoneconfigurator.fragment.PCTransitionDialogFragment;
import com.royalenfield.reprime.ui.phoneconfigurator.listener.ShowLoginListener;
import com.royalenfield.reprime.ui.phoneconfigurator.presenter.WebAppInterface;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.utils.REConstants;

@SuppressLint("SetJavaScriptEnabled")
public class PaymentWebViewActivity extends AppCompatActivity implements ShowLoginListener, INetworkStateListener {

    private String variantId;
    private String savedConfig;
    private String reactFlag;
    boolean loadingFinished = true;
    boolean redirect = false;
    private ProgressDialog mProgressDialog;
    private WebView webView;
    private String bikeID;
    private String configID;
    PCTransitionDialogFragment dialogFragment;
    private ImageView loader;
    private boolean flag = true;
    private String currentUrl;
    private Handler mHandler = new Handler();
    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    private NetworkConnectionReceiver mConnectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pcwebview);
        getIntentData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }

    public void getIntentData() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        variantId = getIntent().getStringExtra(PCUtils.PC_VARIANT_ID);
        savedConfig = getIntent().getStringExtra(PCUtils.PC_SAVED_CONFIG);
        reactFlag = getIntent().getStringExtra(PCUtils.PC_REACT_FLAG);
        bikeID = getIntent().getStringExtra(PCUtils.PC_BIKE_ID);
        configID = getIntent().getStringExtra(PCUtils.PC_CONFIG_ID);
        Gson gson = new Gson();
        if (getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_BOOK_NOW) || getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_BALANCE_PAMENT)) {
            initView();
        } else {
            initLoading();
        }
        Log.e("Web_URl", getIntent().getStringExtra(PCUtils.URL_PAYMENT) + "");
    }

    private void initLoading() {
        loader = findViewById(R.id.loader);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            loader.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                loader.setVisibility(View.GONE);
                initView();
            }, 2000);
        } else {
            loader.setVisibility(View.GONE);
            initView();
        }
    }


    private void initView() {
        loadingFinished = true;
        redirect = false;
        webView = findViewById(R.id.webview);
        webView.setBackgroundColor(Color.parseColor("#141518"));
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setVisibility(View.VISIBLE);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 80) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        // mProgressDialog.cancel();
                        mProgressDialog.dismiss();
                    }
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.requestFocusFromTouch();
        webView.addJavascriptInterface(new WebAppInterface(this, webView, this), "Android");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                currentUrl = urlNewString;
                if (!loadingFinished) {
                    redirect = true;
                }
                loadingFinished = false;


                if (urlNewString.startsWith("tel:")) {
                    Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(urlNewString));
                    startActivity(tel);
                    return true;
                } else if (urlNewString.contains("mailto:")) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(urlNewString)));
                    return true;

                } else {
                    view.loadUrl(urlNewString);
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                if (!(PaymentWebViewActivity.this).isFinishing()) {
                    if (mProgressDialog == null)
                        mProgressDialog = PCUtils.showLoadingDialog(PaymentWebViewActivity.this);
                    else if (mProgressDialog != null && !mProgressDialog.isShowing())
                        mProgressDialog = PCUtils.showLoadingDialog(PaymentWebViewActivity.this);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                flag = false;
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    //   mProgressDialog.cancel();
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (flag) {
                }
            }
        });
        if (reactFlag.equalsIgnoreCase(PCUtils.PC_CONFIG_NOW)) {
            webView.loadUrl(REConstants.MIY_URL + "/" + variantId + PCUtils.PC_PLATFORM);
        } else if (reactFlag.equalsIgnoreCase(PCUtils.PC_BOOK_NOW)) {
           // webView.loadUrl(REConstants.MIY_URL + "/book/" + pcMotorcycleVariantModel.getModelID() + "/" + pcMotorcycleVariantModel.getVariantID() + PCUtils.PC_PLATFORM);
        } else if (reactFlag.equalsIgnoreCase(PCUtils.PC_SAVED_CONFIG)) {
            webView.loadUrl(REConstants.MIY_URL + "/" + bikeID + "/" + configID + PCUtils.PC_PLATFORM);
        } else if (reactFlag.equalsIgnoreCase(PCUtils.PC_BALANCE_PAMENT)) {
            webView.loadUrl(getIntent().getStringExtra(PCUtils.URL_PAYMENT));
            // webView.loadUrl("file:///android_asset/build/index.html");
        }
    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            super.finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.getUrl().contains("billdesk.com")) {
            if (webView.canGoBack()) {
                webView.clearHistory();
                webView.goBack();
            } else {
                super.onBackPressed();
            }
        }
        //disable back button
    }

    @Override
    public void showLoginActivity() {
        PCTransitionShowDialog();
    }

    @Override
    public void gotoSavedConfigScreen() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pc_transition_fragment);
        dialog.show();
        new Handler().postDelayed(() -> {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dialogFragment != null) {
            dialogFragment.dismissAllowingStateLoss();
        }
        REApplication.getInstance().setNetworkChangeListener(this);
    }

    private void PCTransitionShowDialog() {
        dialogFragment = PCUtils.showDialog(this);
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            //Do something after 2s
            Intent intent = new Intent(PaymentWebViewActivity.this, UserOnboardingActivity.class);
            intent.putExtra(PCUtils.PC_CALLING_ACTIVITY, LoginActivity.CODE_PC_ACTIVITY);
            startActivityForResult(intent, LoginActivity.CODE_PC_ACTIVITY);
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialogFragment != null) {
            dialogFragment.dismissAllowingStateLoss();
        }
        REApplication.getInstance().setNetworkChangeListener(null);
    }

    @Override
    public void chnageOrientation(String type) {
        Log.e("ORIENTATION", type);

        if (type.equalsIgnoreCase("portrait"))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_PC_ACTIVITY) {
            dialogFragment.dismissAllowingStateLoss();
            String phoneNumber = REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone() + "|" + REApplication.getInstance().getUserTokenDetails();
            webView.loadUrl("javascript:fetchUserDetailsFromApp('" + phoneNumber + "')");
        }
    }

    @Override
    public void finishWebView(String error) {
        int orientation = getResources().getConfiguration().orientation;
        if (getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_BOOK_NOW) || orientation == Configuration.ORIENTATION_PORTRAIT) {
            finishView(error);
        } else {
            PCUtils.showDialog(this);
            new Handler().postDelayed(() -> {
                //Do something after 2s
                finishView(error);
            }, 2000);
        }
    }

    private void finishView(String error) {
        webView.post(() -> {
            if (error.length() > 0)
                PCUtils.showErrorDialog(this, error);
            else finish();
        });
    }

    @Override
    public void onNetworkConnect() {
        webView.loadUrl("javascript:isNetworkAvailable(true)");
    }

    @Override
    public void onNetworkDisconnect() {
        webView.loadUrl("javascript:isNetworkAvailable(false)");
    }


}
