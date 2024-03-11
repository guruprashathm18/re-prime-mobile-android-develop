package com.royalenfield.reprime.ui.home.homescreen.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

public class CustomerCareActivity extends REBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_care);
        initViews();
    }

    private void initViews() {
        TextView tvHeader = findViewById(R.id.tv_search_banner);
        tvHeader.setText(getString(R.string.text_contact_us).toUpperCase());
        TextView txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtPhoneNumber.setText(REConstants.CC_NUMBER);
        TextView txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(REConstants.EXTRA_EMAIL);

        findViewById(R.id.llEmail).setOnClickListener(this);
        findViewById(R.id.llPhoneNumber).setOnClickListener(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        if (BuildConfig.FLAVOR.contains("Apac")||BuildConfig.FLAVOR.contains("Rena")||BuildConfig.FLAVOR.contains("Latm")||BuildConfig.FLAVOR.contains("EU")) {
            findViewById(R.id.llPhoneNumber).setVisibility(View.GONE);

        }

    }

    @Override
    public void onClick(View view) {
        Bundle  params = new Bundle();
        switch (view.getId()) {
            case R.id.llEmail:

                params.putString("eventCategory", "Contact Us");
                params.putString("eventAction", "Email clicked");
                REUtils.logGTMEvent(REConstants.KEY_CONTACT_US_GTM, params);
                sendEmail();
                break;
            case R.id.llPhoneNumber:
                //callCustomerCare();
                params.putString("eventCategory", "Contact Us");
                params.putString("eventAction", "Phone clicked");
                REUtils.logGTMEvent(REConstants.KEY_CONTACT_US_GTM, params);
                checkAndRequestCallPermissions(getApplicationContext(), CustomerCareActivity.this, REUtils.getREKeys().getCustomerCare(),
                        CALL_PERMISSIONS_REQUESTS, this);
                break;
            case R.id.back_image:
                finish();
                overridePendingTransition(0, R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }

    private void callCustomerCare() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + REConstants.CC_NUMBER));
        startActivity(callIntent);
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{REConstants.EXTRA_EMAIL});
//        i.putExtra(Intent.EXTRA_SUBJECT, REConstants.EXTRA_SUBJECT);
//        i.putExtra(Intent.EXTRA_TEXT, REConstants.EXTRA_TEXT);
        try {
            startActivity(Intent.createChooser(i, getString(R.string.text_send_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CustomerCareActivity.this, R.string.err_not_installed_email_app, Toast.LENGTH_SHORT).show();
        }
    }
}
