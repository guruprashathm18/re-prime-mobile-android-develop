package com.royalenfield.reprime.ui.wonderlust.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.network.listener.INetworkStateListener;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.phoneconfigurator.fragment.PCTransitionDialogFragment;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.wonderlust.interfaces.CommonWebAppInterface;
import com.royalenfield.reprime.ui.wonderlust.interfaces.WebviewListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REDialogUtils;
import com.royalenfield.reprime.utils.REUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

@SuppressLint("SetJavaScriptEnabled")
public class WLWebViewActivity extends AppCompatActivity implements INetworkStateListener,WebviewListener {

    boolean loadingFinished = true;
    boolean redirect = false;
    private ProgressDialog mProgressDialog;
    private WebView webView;
    private String bikeID;
    private String configID;
    PCTransitionDialogFragment dialogFragment;
    private ImageView loader;
    private boolean isShowing;
    private Handler mHandler = new Handler();
    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    private boolean pageLoaded;
    private ProgressDialog mDialogMain;
    private boolean hasError;
    private String permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int EXTERNAL_WRITE_PERMISSIONS = 10;
    private String url, userAgent, contentDisposition, mimeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pcwebview);
        loader = findViewById(R.id.loader);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        bikeID = getIntent().getStringExtra(PCUtils.PC_BIKE_ID);
        configID = getIntent().getStringExtra(PCUtils.PC_CONFIG_ID);
        if (!(WLWebViewActivity.this).isFinishing()) {
            if (mProgressDialog == null)
                mProgressDialog = PCUtils.showLoadingDialog(WLWebViewActivity.this);
            else if (mProgressDialog != null && !mProgressDialog.isShowing())
                mProgressDialog = PCUtils.showLoadingDialog(WLWebViewActivity.this);
        }
        final Handler handler = new Handler();
        handler.postDelayed(this::initView, 200);


    }


    private void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadingFinished = true;
        redirect = false;
        webView = findViewById(R.id.webview);
        webView.setBackgroundColor(Color.parseColor("#141518"));
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        if (REUtils.isNetworkAvailable(getApplicationContext())) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//Decide whether to retrieve data from the network based on cache-control.
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//If there is no network, it is obtained locally, i.e. offline loading.
        }

        webView.getSettings().setDomStorageEnabled(true); // Turn on the DOM storage API function
        webView.getSettings().setDatabaseEnabled(true);   //Turn on the database storage API function
        //webView.getSettings().setAppCacheEnabled(true);//Open Application Caches
        String dbPath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setDatabasePath(dbPath);
        // The app can have a cache
       // webView.getSettings().setAppCacheEnabled(true);
       // String appCaceDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
       // webView.getSettings().setAppCachePath(appCaceDir);
        Log.e("USER_AGENT", webView.getSettings().getUserAgentString());
        //Setting up the Application Caches cache directory
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
                    pageLoaded = true;
                    if (!WLWebViewActivity.this.isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }

            }


        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.requestFocusFromTouch();
        webView.addJavascriptInterface(new CommonWebAppInterface(this, webView, this), "Android");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
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
                hasError = false;
                Handler timeoutHandler = new Handler();
                if (mProgressDialog != null && !mProgressDialog.isShowing())
                    mProgressDialog.show();
                pageLoaded = false;
                Runnable run = () -> {

                    if (!WLWebViewActivity.this.isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {

                        mProgressDialog.dismiss();
                        mProgressDialog = null;


                        if (hasError)
                            return;

                        if (!pageLoaded) {
                            REUtils.showErrorDialog(WLWebViewActivity.this, getResources().getString(R.string.web_view_error), new REUtils.OnDialogButtonClickListener() {
                                @Override
                                public void onOkCLick() {
                                    finish();
                                }
                            });
                        }
                    }
                };
                timeoutHandler.postDelayed(run, 30 * 1000);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pageLoaded = true;
                if (WLWebViewActivity.this.isDestroyed())
                    return;
                if (!WLWebViewActivity.this.isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
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
                hasError = true;
                if (!WLWebViewActivity.this.isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }


            }
        });

        webView.post(() -> {
            if (getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_OPEN_WONDERLUST)) {
                webView.loadUrl(REConstants.WANDERLUST_URL);
            } else {
                Log.e("URL", REConstants.SERVICE_URL + getIntent().getStringExtra(PCUtils.PC_CHESSIS_NO));
                webView.loadUrl(REConstants.SERVICE_URL + "/" + getIntent().getStringExtra(PCUtils.PC_CHESSIS_NO));
            }

        });
        webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {

            this.url = url;
            this.userAgent = userAgent;
            this.contentDisposition = contentDisposition;
            this.mimeType = mimeType;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissions(url, userAgent, contentDisposition, mimeType, contentLength);
            } else
                downLoadFile(url, userAgent, contentDisposition, mimeType);


        });

    }

    private void downLoadFile(String url, String userAgent, String contentDisposition, String mimeType) {
//
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setMimeType(mimeType);
        String cookies = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookies);
        request.addRequestHeader("User-Agent", userAgent);
        request.setDescription("Downloading File...");
        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                        url, contentDisposition, mimeType));
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
        // webView.loadUrl(CommonWebAppInterface.getBase64StringFromBlobUrl(url));
        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermissions(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
        int result, p;
        List<String> listPermissionsNeeded = new ArrayList<>();
        result = checkSelfPermission(permissions);
        if (result != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_WRITE_PERMISSIONS);
        } else {
            downLoadFile(url, userAgent, contentDisposition, mimeType);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_WRITE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case EXTERNAL_WRITE_PERMISSIONS:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        downLoadFile(url, userAgent, contentDisposition, mimeType);
                    } else {

                    }
                    break;

            }
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public void showLoading() {
        Log.e("React URL:", "Webview show loading called");
        hideLoading();
        mDialogMain = REDialogUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        Log.e("React URL:", "Webview show loading called");
        if (mDialogMain != null && mDialogMain.isShowing()) {
            mDialogMain.cancel();
            mDialogMain = null;
            REDialogUtils.removeHandlerCallbacks();
        }
    }

    @Override
    public void onBackPressed() {
        // if (webView != null &&webView.getUrl()!=null&& webView.getUrl().contains("billdesk.com")) {
//            if (webView.canGoBack()) {
//                webView.clearHistory();
//                webView.goBack();
//            } else {
        super.onBackPressed();
        // }
        //  }

        //disable back button
    }

    @Override
    public void showLoginActivity() {
        PCTransitionShowDialog();
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
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isShowing = true;
            dialogFragment = PCUtils.showDialog(this);
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                //Do something after 2s
                gotoLogin();
                isShowing = false;
            }, 2000);
        } else {
            gotoLogin();
        }
    }

    private void gotoLogin() {
        Intent intent = new Intent(WLWebViewActivity.this, UserOnboardingActivity.class);
        intent.putExtra(PCUtils.PC_CALLING_ACTIVITY, LoginActivity.CODE_PC_ACTIVITY);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            intent.putExtra(PCUtils.PC_BOOK_NOW, "");
        startActivityForResult(intent, LoginActivity.CODE_PC_ACTIVITY);
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
        new Handler().postDelayed(() -> runOnUiThread(() -> getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)), 1000);
        if (type.equalsIgnoreCase("portrait")) {

            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {


                if (dialogFragment != null
                        && dialogFragment.getDialog() != null
                        && dialogFragment.getDialog().isShowing()
                        && !dialogFragment.isRemoving()) {
                    //dialog is showing so do something
                } else {
                    //dialog is not showing
                    if (!isShowing)
                        dialogFragment = PCUtils.showDialog(WLWebViewActivity.this);
                    isShowing = true;
                }

                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    //Do something after 2s
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    if (dialogFragment != null) {
                        isShowing = false;
                        dialogFragment.dismissAllowingStateLoss();
                    }

                }, 2000);
            }
        } else {
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                if (!isShowing)
                    dialogFragment = PCUtils.showDialog(WLWebViewActivity.this);
                isShowing = true;

                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    //Do something after 2s
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


                    if (dialogFragment != null) {
                        dialogFragment.dismissAllowingStateLoss();
                        isShowing = false;
                    }

                }, 2000);
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_PC_ACTIVITY) {
            if (dialogFragment != null)
                dialogFragment.dismissAllowingStateLoss();
            String phoneNumber = REApplication.getInstance().getUserTokenDetails();
            webView.loadUrl("javascript:recieveJWTToken('" + phoneNumber + "')");
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
        if (webView != null)
            webView.loadUrl("javascript:isNetworkAvailable(true)");
    }

    @Override
    public void onNetworkDisconnect() {
        if (webView != null)
            webView.loadUrl("javascript:isNetworkAvailable(false)");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mHandler.post(decor_view_settings);
        }
    }


    private Runnable decor_view_settings = new Runnable() {
        public void run() {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }


    }

    @Override
    public void navigateToScreen(String id) {
    }

    @Override
    public void showHideKeyboard(boolean isShown) {

    }
}
