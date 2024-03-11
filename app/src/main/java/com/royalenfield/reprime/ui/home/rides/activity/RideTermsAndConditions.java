package com.royalenfield.reprime.ui.home.rides.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.utils.REConstants;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_RTC;
import static com.royalenfield.reprime.utils.REConstants.KEY_TNC;

public class RideTermsAndConditions extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener {

    private String defaultURL = "file:///android_asset/create_ride_terms_conditions.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_terms_and_conditions);
        initViews();
    }

    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.ride_terms_header);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.text_terms_and_conditions));
        WebView webView = findViewById(R.id.webView_settings);
        String url = defaultURL;
        try {
            url = REPreference.getInstance().getString(REApplication.getAppContext(), KEY_RTC).length() == 0 ? defaultURL : REPreference.getInstance().getString(REApplication.getAppContext(), KEY_RTC);
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        webView.loadUrl(url);
    }

    public void onAcceptConditions(View v) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("terms_cons", "accepted");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void navigateBack() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onNavigationIconClick() {
        navigateBack();
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }
}
