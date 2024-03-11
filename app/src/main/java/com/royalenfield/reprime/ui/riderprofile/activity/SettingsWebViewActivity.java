package com.royalenfield.reprime.ui.riderprofile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;


/**
 * This is the webView for Terms & Conditions and FAQ settings
 */
public class SettingsWebViewActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener {

    private String SETTINGS_TYPE;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_webview);
        getIntentData();
        initializeViews();
    }

    /**
     * Gets intent data from settings adapter
     */
    private void getIntentData() {
        Intent intent = getIntent();
        SETTINGS_TYPE = intent.getStringExtra("settings_type");
    }

    /**
     * Initializes all the views for the UI....
     */
    private void initializeViews() {
        TitleBarView mTitleBarView = findViewById(R.id.settings_header);
        mTitleBarView.bindData(this, R.drawable.back_arrow, SETTINGS_TYPE);
        webView = findViewById(R.id.webView_settings);
        loadPage();
    }

    private void loadPage() {
        if (SETTINGS_TYPE.equals(getApplicationContext().getResources().getString(R.string.text_terms_and_conditions))) {
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.setWebViewClient(new WebViewController());
            webView.loadUrl("file:///android_asset/terms_sign.html");
//            webView.requestFocus();
        } else {
            webView.loadUrl("file:///android_asset/faqs.html");
        }
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
