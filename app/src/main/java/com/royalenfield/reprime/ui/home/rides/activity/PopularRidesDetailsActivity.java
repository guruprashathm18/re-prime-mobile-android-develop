package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;

public class PopularRidesDetailsActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener {

    private final String TAG = "PopularRidesDetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_rides_details);
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();

        final String strRidesTitle = intent.getExtras().getString("pop_rides_title");
        final String strRidesUrl = intent.getExtras().getString("pop_rides_url");

        TitleBarView mTitleBarView = findViewById(R.id.popular_ride_header);
        mTitleBarView.bindData(this, R.drawable.icon_close, strRidesTitle);

        final WebView webView = findViewById(R.id.webView_popular_details);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int newProgress) {
               // Log.e(TAG, "Page loading : " + newProgress + "%");

                if (newProgress == 100) {
                    // Page loading finish
                    //Log.e(TAG, "Page Loaded.");
                    new MyWebViewClient().onPageFinished(webView, strRidesUrl);
                }
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(strRidesUrl);

    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(0, R.anim.slide_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.slide_down);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

           // Log.e(TAG, "Page Loaded");
            hideLoading();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
           // Log.e(TAG, "Page Started");
            showLoading();
            super.onPageStarted(view, url, favicon);
        }
    }
}
