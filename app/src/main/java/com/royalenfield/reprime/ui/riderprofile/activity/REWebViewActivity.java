package com.royalenfield.reprime.ui.riderprofile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnPaymentCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.homescreen.view.HelpAndSupportActivity;
import com.royalenfield.reprime.ui.wonderlust.interfaces.CommonWebAppInterface;
import com.royalenfield.reprime.ui.wonderlust.interfaces.WebviewListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.D_RSA;
import static com.royalenfield.reprime.utils.REConstants.KEY_D_RSA_URL;
import static com.royalenfield.reprime.utils.REConstants.KEY_EW;
import static com.royalenfield.reprime.utils.REConstants.KEY_FAQ;
import static com.royalenfield.reprime.utils.REConstants.KEY_NAVIGATION_FROM;
import static com.royalenfield.reprime.utils.REConstants.KEY_PRIVACY;
import static com.royalenfield.reprime.utils.REConstants.KEY_RSA;
import static com.royalenfield.reprime.utils.REConstants.KEY_RSA_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_SUPPORT_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_TNC;
import static com.royalenfield.reprime.utils.REConstants.KEY_TNC_DRSA;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;
import static com.royalenfield.reprime.utils.REConstants.USERDATA;
import static com.royalenfield.reprime.utils.REFirestoreConstants.REGISTRATION_POLICIES_TYPE;


/**
 * This is the webView for Terms & Conditions and FAQ settings
 */
public class REWebViewActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener, WebviewListener,
        OnPaymentCallback {

    private String mInputType;
    private int mPosition, mAdapterPosition;
    private WebView webView;
    private TitleBarView mTitleBarView;
    private String mPaymentCheckSum;
    private String mPaymentStatusMessage;
private ConstraintLayout constraint;
private TextView txtIAgree;
private  TextView ttDisclaimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra(KEY_NAVIGATION_FROM))
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings_webview);
        getIntentData();
        if(mInputType.equalsIgnoreCase(D_RSA)){
            Bundle paramsScr = new Bundle();
            paramsScr.putString("screenname", "RSA screen");
            REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        }
        initializeViews();
    }

    /**
     * Gets intent data from settings adapter
     */
    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mInputType = bundle.getString(REConstants.SETTING_ACTIVITY_INPUT_TYPE, null);
            mPosition = bundle.getInt(REConstants.RIDES_LIST_POSITION, 0);
            mAdapterPosition = bundle.getInt(REConstants.ADAPTER_POSITION, 0);
            mPaymentCheckSum = bundle.getString(REConstants.PAYMENT_CHECKSUM);
        }
    }

    /**
     * Initializes all the views for the UI....
     */
    private void initializeViews() {
        mTitleBarView = findViewById(R.id.settings_header);
        webView = findViewById(R.id.webView_settings);
        constraint=findViewById(R.id.constraint_consent);
        txtIAgree=findViewById(R.id.btn_agree);
        ttDisclaimer=findViewById(R.id.txt_disclaimer);
//        Spannable word = new SpannableString(getResources().getString(R.string.disclaimer_lbl));
//
//        word.setSpan(new ForegroundColorSpan(Color.RED), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        ttDisclaimer.setText(word);
//        Spannable wordTwo = new SpannableString(getResources().getString(R.string.disclaimer_text));
//        ttDisclaimer.append(" ");
//        wordTwo.setSpan(new ForegroundColorSpan(Color.WHITE), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ttDisclaimer.append(wordTwo);
        customTextView(ttDisclaimer);
        txtIAgree.setOnClickListener(v -> {
            Bundle params = new Bundle();
            params.putString("eventCategory", "RSA");
            params.putString("eventAction", "I Agree clicked");
            REUtils.logGTMEvent(KEY_RSA_GTM,params);
            constraint.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            String url=getIntent().getExtras().getString(KEY_D_RSA_URL);
            loadWebView(url);
        });
        try {
            loadPage();
        } catch (PreferenceException e) {
            RELog.e(e);
        }
    }
    private void customTextView(TextView view) {
        Typeface mTypefaceMontserratRegular = ResourcesCompat.getFont(this,
                R.font.montserrat_regular);
        Typeface typeface_montserrat_semibold = ResourcesCompat.getFont(this,
                R.font.montserrat_semibold);

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                getString(R.string.disclaimer_lbl));
//        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), 0,
//                getString(R.string.disclaimer_lbl).length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.RED), 0, spanTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanTxt.append(" ");
          spanTxt.append(getString(R.string.by_continuing_further));

        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), spanTxt.length() -
                getString(R.string.by_continuing_further)
                        .length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), getString(R.string.disclaimer_lbl).length(), spanTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanTxt.append(" ");
        spanTxt.append(getString(R.string.text_terms_conditions));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Bundle params=new Bundle();
                params.putString("eventCategory", "RSA");
                params.putString("eventAction", getString(R.string.text_terms_conditions)+" clicked");
                REUtils.logGTMEvent(KEY_RSA_GTM,params);
                Intent intent = new Intent(REWebViewActivity.this, REWebViewActivity.class);
                intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, getResources().getString(R.string.term_of_condition_drsa));
                startActivity(intent);
                Objects.requireNonNull(REWebViewActivity.this).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);


            }
        }, spanTxt.length() - getString(R.string.text_terms_conditions)
                .length(), spanTxt.length(), 0);
        spanTxt.setSpan(new RECustomTyperFaceSpan(typeface_montserrat_semibold), spanTxt.length()
                - getString(R.string.text_terms_conditions)
                .length(), spanTxt.length(), 0);


        spanTxt.append(" ");
        spanTxt.append(getString(R.string.disclaimer_text));

        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), spanTxt.length() -
                getString(R.string.disclaimer_text)
                        .length(), spanTxt.length(), 0);

        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), getString(R.string.disclaimer_lbl).length(), spanTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);





//        spanTxt.append(getString(R.string.text_privacy_policies));
//        spanTxt.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                Intent intent = new Intent(this, PrivacyPolicyAndTermsActivity.class);
//                intent.putExtra("activity_type", "Privacy Policy");
//                startActivity(intent);
//                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
//            }
//        }, spanTxt.length() - getString(R.string.text_privacy_policies)
//                .length(), spanTxt.length(), 0);
//
//        spanTxt.setSpan(new RECustomTyperFaceSpan(typeface_montserrat_semibold), spanTxt.length() -
//                getString(R.string.text_privacy_policies)
//                        .length(), spanTxt.length(), 0);

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    private void loadPage() throws PreferenceException {
        if (mInputType.equals(REConstants.RIDE_TYPE_POPULAR) || mInputType.equals(REConstants.RIDE_TYPE_MARQUEE)) {
            if (mInputType.equals(REConstants.RIDE_TYPE_POPULAR)) {
                List<PopularRidesResponse> mPopularRidesResponse = RERidesModelStore.getInstance().getPopularRidesResponse();
                mTitleBarView.bindData(this, R.drawable.back_arrow, mPopularRidesResponse.
                        get(mPosition).getPolicyUrls().get(mAdapterPosition).get(REGISTRATION_POLICIES_TYPE));
                loadWebView(REUtils.getMobileappbaseURLs().getAssetsURL() +
                        mPopularRidesResponse.get(mPosition).getPolicyUrls().get(mAdapterPosition).
                                get(REFirestoreConstants.REGISTRATION_POLICIES_URL));
            } else {
                List<MarqueeRidesResponse> mMarqueeRidesResponse = RERidesModelStore.getInstance().getMarqueeRidesResponse();
                mTitleBarView.bindData(this, R.drawable.back_arrow, mMarqueeRidesResponse.
                        get(mPosition).getPolicyUrls().get(mAdapterPosition).get(REGISTRATION_POLICIES_TYPE));
                loadWebView(REUtils.getMobileappbaseURLs().getAssetsURL() +
                        mMarqueeRidesResponse.get(mPosition).getPolicyUrls().get(mAdapterPosition).
                                get(REFirestoreConstants.REGISTRATION_POLICIES_URL));
            }
//            webView.loadUrl();
        } else if (mInputType.equals(REConstants.OUR_WORLD_EVENTS)) {
            List<EventsResponse> mEventsResponse = RERidesModelStore.getInstance().getOurWorldEventsResponse();
            mTitleBarView.bindData(this, R.drawable.back_arrow, mEventsResponse.get(mPosition).
                    getPolicyUrls().get(mAdapterPosition).get(REGISTRATION_POLICIES_TYPE));
            loadWebView(REUtils.getMobileappbaseURLs().getAssetsURL() +
                    mEventsResponse.get(mPosition).getPolicyUrls().get(mAdapterPosition).
                            get(REFirestoreConstants.REGISTRATION_POLICIES_URL));
        } else if (mInputType.equals(REConstants.TYPE_PAYMENT) && mPaymentCheckSum != null && !mPaymentCheckSum.isEmpty()) {
            mTitleBarView.bindData(this, R.drawable.icon_close, mInputType);
            loadWebView(REUtils.getMobileappbaseURLs().getPaymentURL() +
                    mPaymentCheckSum);
            //Starts listening to the Payment document in firestore
            FirestoreManager.getInstance().getPaymentStatus(this);
        }
        else if(mInputType.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.term_of_condition))){
Log.e("data", REPreference.getInstance().getString(REApplication.getAppContext(), KEY_TNC));
            Bundle paramsScr = new Bundle();
            paramsScr.putString("screenname", "Terms and Conditions screen");
            REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
            mTitleBarView.bindData(this, R.drawable.icon_close, getApplicationContext().getResources().getString(R.string.text_terms_and_conditions));
                loadWebView(  REPreference.getInstance().getString(REApplication.getAppContext(), KEY_TNC).length()==0?REConstants.DEFAULT_RE_URL:REPreference.getInstance().getString(REApplication.getAppContext(), KEY_TNC));
        }
        else if(mInputType.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.term_of_condition_drsa))){
            mTitleBarView.bindData(this, R.drawable.icon_close, getApplicationContext().getResources().getString(R.string.text_terms_and_conditions));
            loadWebView(  REPreference.getInstance().getString(REApplication.getAppContext(), KEY_TNC_DRSA).length()==0?REConstants.DEFAULT_RE_URL:REPreference.getInstance().getString(REApplication.getAppContext(), KEY_TNC_DRSA));
            Bundle params = new Bundle();
            params.putString("screenname", "Terms and Conditions screen");
            REUtils.logGTMEvent(KEY_SCREEN_GTM, params);
        } else if(mInputType.equalsIgnoreCase(D_RSA)){
            mTitleBarView.bindData(this, R.drawable.icon_close, getApplicationContext().getResources().getString(R.string.txt_rsa));
           showConsentScreen();
//            String url=getIntent().getExtras().getString(KEY_D_RSA_URL);
//            loadWebView(url);

        } else {
            mTitleBarView.bindData(this, R.drawable.back_arrow, mInputType);
			if (mInputType.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.text_privacy_policies_camel))) {
				loadWebView(REPreference.getInstance().getString(REApplication.getAppContext(), KEY_PRIVACY).length() == 0 ? REConstants.DEFAULT_RE_URL : REPreference.getInstance().getString(REApplication.getAppContext(), KEY_PRIVACY));
			}
           else if (mInputType.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.text_terms_and_conditions))) {
                Bundle paramsScr = new Bundle();
                paramsScr.putString("screenname", "Terms and Conditions screen");
                REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
                loadWebView(  REPreference.getInstance().getString(REApplication.getAppContext(), KEY_TNC).length()==0?REConstants.DEFAULT_RE_URL:REPreference.getInstance().getString(REApplication.getAppContext(), KEY_TNC));
            } else if (mInputType.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.road_side_assistance))) {

                Bundle paramsScr = new Bundle();
                paramsScr.putString("screenname", "RSA Contact screen");
                REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
                loadWebView(  REPreference.getInstance().getString(REApplication.getAppContext(), KEY_RSA).length()==0?REConstants.DEFAULT_RE_URL:REPreference.getInstance().getString(REApplication.getAppContext(), KEY_RSA));
            } else if (mInputType.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.text_extended_warranty))) {
                Bundle paramsScr = new Bundle();
                paramsScr.putString("screenname", "Ride Sure screen");
                REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
                loadWebView(  REPreference.getInstance().getString(REApplication.getAppContext(), KEY_EW).length()==0?REConstants.DEFAULT_RE_URL:REPreference.getInstance().getString(REApplication.getAppContext(), KEY_EW));
            }

            else {

                Bundle paramsScr = new Bundle();
                paramsScr.putString("screenname", "FAQs screen");
                REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
                loadWebView(  REPreference.getInstance().getString(REApplication.getAppContext(), KEY_FAQ).length()==0?REConstants.DEFAULT_RE_URL:REPreference.getInstance().getString(REApplication.getAppContext(), KEY_FAQ));
            }
        }
    }

    private void showConsentScreen() {
constraint.setVisibility(View.VISIBLE);
webView.setVisibility(View.GONE);

    }

    private void loadWebView(String strRidesUr) {
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                enableTheLocation();
                callback.invoke(origin, true, false);
            }
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // Page loading finish
                    new MyWebViewClient().onPageFinished(webView, strRidesUr);
                }
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setGeolocationDatabasePath( getFilesDir().getPath() );
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setDomStorageEnabled(true);
    //    webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        if (mInputType.equalsIgnoreCase(D_RSA))
            webView.addJavascriptInterface(new CommonWebAppInterface(this, webView, this), "Android");

        webView.loadUrl(strRidesUr);
    }

    @Override
    public void onNavigationIconClick() {
        Intent intent = new Intent();
        intent.putExtra(REConstants.PAYMENT_STATUS_MESSAGE, mPaymentStatusMessage);
        setResult(REConstants.REQUESTCODE_PAYMENTSTATUS, intent);
        if(mInputType.equalsIgnoreCase(D_RSA)||mInputType.equalsIgnoreCase(getResources().getString(R.string.term_of_condition_drsa))){
            Bundle params = new Bundle();
            params.putString("eventCategory", "RSA");
            params.putString("eventAction", "Close clicked");
            REUtils.logGTMEvent(KEY_RSA_GTM,params);
        }
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        if (mInputType != null && mInputType.equals(REConstants.TYPE_PAYMENT)) {
            //Don't do anything
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
        }
    }

    @Override
    public void onPaymentStatus(String message, String caseId) {
        mPaymentStatusMessage = message;
        if (message != null && !message.equals("PENDING")) {
            Intent intent = new Intent();
            intent.putExtra(REConstants.PAYMENT_STATUS_MESSAGE, message);
            setResult(REConstants.REQUESTCODE_PAYMENTSTATUS, intent);
            finish();
        }
    }

    @Override
    public void showLoginActivity() {
    }

    @Override
    public void finishWebView(String error) {
    }

    @Override
    public void chnageOrientation(String type) {
    }

    @Override
    public void navigateToScreen(String id) {
    }

    @Override
    public void showHideKeyboard(boolean isShown) {

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                if(mInputType.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.road_side_assistance))) {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Support");
                    params.putString("eventAction", "RSA");
                    params.putString("eventLabel", "Toll free call");
                    REUtils.logGTMEvent(KEY_SUPPORT_GTM, params);
                }
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                view.reload();
                return true;
            }
            else if (url.contains("mailto:")) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;

            }
            else{
                view.loadUrl(url);
            }
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideLoading();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showLoading();
            super.onPageStarted(view, url, favicon);
        }
    }
}
