package com.royalenfield.reprime.ui.phoneconfigurator.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.network.listener.INetworkStateListener;
import com.royalenfield.reprime.network.receiver.NetworkConnectionReceiver;
import com.royalenfield.reprime.ui.home.rides.activity.CreateRideActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.phoneconfigurator.fragment.PCTransitionDialogFragment;
import com.royalenfield.reprime.ui.phoneconfigurator.listener.ShowLoginListener;
import com.royalenfield.reprime.ui.phoneconfigurator.presenter.WebAppInterface;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.riderprofile.activity.REWebViewActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REDialogUtils;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.generateLogs;
import static com.royalenfield.reprime.utils.REConstants.KEY_NAVIGATION_FROM;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;

@SuppressLint("SetJavaScriptEnabled")
public class PCWebViewActivity extends AppCompatActivity implements ShowLoginListener, INetworkStateListener {

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
    private NetworkConnectionReceiver mConnectivityReceiver;
    private boolean isShowing;
    private Handler mHandler = new Handler();
    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    private boolean pageLoaded;
    private ProgressDialog mDialogMain;
    private boolean hasError;

    protected void onShowKeyboard(int keyboardHeight) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


    }
    protected void onHideKeyboard() {
        mHandler.postDelayed(decor_view_settings,200);
        new Handler().postDelayed(() -> {
            ViewGroup.LayoutParams params = rootLayout.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            rootLayout.setPadding(0,0,0,0);
            rootLayout.setLayoutParams(params);
        },500);

    }

    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = (ViewGroup) findViewById(R.id.rel_root);

        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new
                                                                           ViewTreeObserver.OnGlobalLayoutListener() {
                                                                               @Override
                                                                               public void onGlobalLayout() {
                                                                                   Rect r = new Rect();
                                                                                   rootLayout.getWindowVisibleDisplayFrame(r);
                                                                                   int screenHeight = rootLayout.getRootView().getHeight();
                                                                                   int keypadHeight = screenHeight - r.bottom;
                                                                                   if (keypadHeight > screenHeight * 0.15) {
                                                                                       onShowKeyboard(keypadHeight);
                                                                                       rootLayout.setPadding(0, 0, 0, keypadHeight);
                                                                                       //  Toast.makeText(MainActivity.this,"Keyboard is showing",Toast.LENGTH_LONG).show();
                                                                                   } else {

                                                                                       onHideKeyboard();
                                                                                       //  Toast.makeText(MainActivity.this,"keyboard closed",Toast.LENGTH_LONG).show();
                                                                                   }
                                                                               }
                                                                           });
        keyboardListenersAttached = true;
    }
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
        attachKeyboardListeners();



        variantId = getIntent().getStringExtra(PCUtils.PC_VARIANT_ID);
        savedConfig = getIntent().getStringExtra(PCUtils.PC_SAVED_CONFIG);
        reactFlag = getIntent().getStringExtra(PCUtils.PC_REACT_FLAG);
        bikeID = getIntent().getStringExtra(PCUtils.PC_BIKE_ID);
        configID = getIntent().getStringExtra(PCUtils.PC_CONFIG_ID);
        Gson gson = new Gson();

       // pcMotorcycleVariantModel = gson.fromJson(getIntent().getStringExtra(PCUtils.PC_VARIANT_MODEL), PCMotorcycleVariantModel.class);
        if (getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_BOOK_NOW) || getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_BALANCE_PAMENT) || getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_OPEN_MYO)|| getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_OPEN_ORDER_TRACKING)||getIntent().getStringExtra(PCUtils.PC_REACT_FLAG).equalsIgnoreCase(PCUtils.PC_OPEN_FINANCE)) {
            if (!(PCWebViewActivity.this).isFinishing()) {
                if (mProgressDialog == null)
                    mProgressDialog = PCUtils.showLoadingDialog(PCWebViewActivity.this);
                else if (mProgressDialog != null && !mProgressDialog.isShowing())
                    mProgressDialog = PCUtils.showLoadingDialog(PCWebViewActivity.this);
            }
            final Handler handler = new Handler();
            handler.postDelayed(this::initView, 200);

        } else {
            initLoading();
        }
        }


    private void initLoading() {

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            loader.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                loader.setVisibility(View.GONE);
                initView();
            }, 200);
        } else {
            loader.setVisibility(View.GONE);
            initView();
        }
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
       // webView.getSettings().setAppCacheEnabled(true);//Open Application Caches
        String dbPath =this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setDatabasePath(dbPath);
        // The app can have a cache
      //  webView.getSettings().setAppCacheEnabled(true);
     //   String appCaceDir =this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
      //  webView.getSettings().setAppCachePath(appCaceDir);
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
                    pageLoaded=true;
                    if  (!PCWebViewActivity.this.isFinishing() &&mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        mProgressDialog=null;
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
                hasError=false;
                Handler timeoutHandler=new Handler();
                if(mProgressDialog!=null&&!mProgressDialog.isShowing())
                    mProgressDialog.show();
                pageLoaded = false;
                Runnable run = () -> {

                    if  (!PCWebViewActivity.this.isFinishing() &&mProgressDialog!=null&&mProgressDialog.isShowing()) {

                        mProgressDialog.dismiss();
                        mProgressDialog=null;


                        if(hasError)
                            return;

                        if (!pageLoaded) {
                            REUtils.showErrorDialog(PCWebViewActivity.this, getResources().getString(R.string.web_view_error), new REUtils.OnDialogButtonClickListener() {
                                @Override
                                public void onOkCLick() {
                                    finish();
                                }
                            });
                        }
                    }
                };
                timeoutHandler.postDelayed(run, 30*1000);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                flag = false;
                pageLoaded=true;
                if(PCWebViewActivity.this.isDestroyed())
                    return;
                if  (!PCWebViewActivity.this.isFinishing() &&mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog=null;
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
                hasError=true;
                if  (!PCWebViewActivity.this.isFinishing() &&mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog=null;
                }


            }
        });
        if (reactFlag.equalsIgnoreCase(PCUtils.PC_CONFIG_NOW)) {
            webView.loadUrl(REConstants.MIY_URL + "/" + variantId + PCUtils.PC_PLATFORM);
        } else if (reactFlag.equalsIgnoreCase(PCUtils.PC_OPEN_MYO)) {
            webView.post(() -> {
                    webView.loadUrl(REConstants.MIY_URL);
            });

        }
        else if(reactFlag.equalsIgnoreCase(PCUtils.PC_OPEN_ORDER_TRACKING)){
            webView.post(() -> {
                Log.e("URL",REConstants.OT_URL+"?paymentCaseId="+getIntent().getStringExtra(PCUtils.ORDER_ID));
                webView.loadUrl(REConstants.OT_URL+"?paymentCaseId="+getIntent().getStringExtra(PCUtils.ORDER_ID));
            });
        }
        else if(reactFlag.equalsIgnoreCase(PCUtils.PC_OPEN_FINANCE)){
            webView.post(() -> {
                Log.e("URL",REConstants.FINANCE_URL);
                webView.loadUrl(REConstants.FINANCE_URL);
            });
        }
        else if (reactFlag.equalsIgnoreCase(PCUtils.PC_BOOK_NOW)) {
         //   webView.loadUrl(REConstants.MIY_URL + "/book/" + pcMotorcycleVariantModel.getModelID() + "/" + pcMotorcycleVariantModel.getVariantID() + PCUtils.PC_PLATFORM);
        } else if (reactFlag.equalsIgnoreCase(PCUtils.PC_SAVED_CONFIG)) {
            webView.loadUrl(REConstants.MIY_URL + "/" + bikeID + "/" + configID + PCUtils.PC_PLATFORM);
        } else if (reactFlag.equalsIgnoreCase(PCUtils.PC_BALANCE_PAMENT)) {
            webView.loadUrl(getIntent().getStringExtra(PCUtils.URL_PAYMENT));
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
        if (webView != null &&webView.getUrl()!=null&& webView.getUrl().contains("billdesk.com")) {
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
        Intent intent = new Intent(PCWebViewActivity.this, UserOnboardingActivity.class);
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
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)),1000);
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
                        dialogFragment = PCUtils.showDialog(PCWebViewActivity.this);
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
                    dialogFragment = PCUtils.showDialog(PCWebViewActivity.this);
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
        if(webView!=null)
        webView.loadUrl("javascript:isNetworkAvailable(true)");
    }

    @Override
    public void onNetworkDisconnect() {
        if(webView!=null)
        webView.loadUrl("javascript:isNetworkAvailable(false)");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mHandler.post(decor_view_settings);
        }
    }


    private Runnable decor_view_settings = new Runnable()
    {
        public void run()
        {
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

        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
        if  (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog=null;
        }


    }
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(PCWebViewActivity.this);

            if(heightDiff <= contentViewTop){
                onHideKeyboard();

                Intent intent = new Intent("KeyboardWillHide");
                broadcastManager.sendBroadcast(intent);
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);

                Intent intent = new Intent("KeyboardWillShow");
                intent.putExtra("KeyboardHeight", keyboardHeight);
                broadcastManager.sendBroadcast(intent);
            }
        }
    };


    public static boolean isConnectionFast(int type, int subType){
        if(type== ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }
}
