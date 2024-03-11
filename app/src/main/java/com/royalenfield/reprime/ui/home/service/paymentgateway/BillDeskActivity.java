package com.royalenfield.reprime.ui.home.service.paymentgateway;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.royalenfield.reprime.R;

/**
 * Activity to load webView (payment gateway page).
 */

public class BillDeskActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_desk);
        webView = findViewById(R.id.billdesk_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        if (getIntent().getExtras() != null && getIntent().getExtras().get("url") != null && getIntent().getExtras().get("checkSumMsg") != null) {
            String htmlData = formHtmlData(getIntent().getExtras().get("url").toString(), getIntent().getExtras().get("checkSumMsg").toString());
            if (htmlData != null) {
                webView.loadData(htmlData, "text/html", "UTF-8");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.clearHistory();
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Generates html data which can be loaded in webView.
     *
     * @param url
     * @param checkSumMessage
     * @return
     */
    public String formHtmlData(String url, String checkSumMessage) {
        String htmlData = " <html>" +
                "<body onload=\"document.frm1.submit()\">" +
                " <form action=\" " + url + " \" name=\"frm1\" method=\"POST\">" +
                "  <input type='hidden' name='msg' value='" + checkSumMessage + "'>" +
                "</form>" +
                "</body>" +
                "</html>";
        return htmlData;
    }
}
