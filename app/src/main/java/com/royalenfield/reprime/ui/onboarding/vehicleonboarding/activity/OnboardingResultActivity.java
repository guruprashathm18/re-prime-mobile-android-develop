package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;


public class OnboardingResultActivity extends REBaseActivity {

    ImageView statusIMG;
    ImageView cancel;
    TextView status,statusContactCC;
    Button action;
    LinearLayout addMoreVehicle;
    String title, image, buttonAction, sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_result);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        sender = getIntent().getStringExtra("sender");
        initViews();
        UIValidation();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        status = findViewById(R.id.title);
        statusContactCC=findViewById(R.id.title_2);
        action = findViewById(R.id.action_button);
        statusIMG = findViewById(R.id.status_img);
        addMoreVehicle = findViewById(R.id.add_more_vehicle);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sender.equalsIgnoreCase("verifyMotorCycle")){
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Vehicle Onboarding");
                    params.putString("eventAction", "Failed Onboarding");
                    params.putString("eventLabel", "close click");
                   REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                }else{
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Vehicle Onboarding");
                    params.putString("eventAction", "Successful Onboarding");
                    params.putString("eventLabel", "close click");
                   REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                }


                REApplication.getInstance().setComingFromVehicleOnboarding(true);
                startActivity(new Intent(OnboardingResultActivity.this, REHomeActivity.class));
                finish();
            }
        });


    }

    private void UIValidation() {
        if(sender.equalsIgnoreCase("verifyMotorCycle")){
            status.setText(R.string.verify_fail_1);
            statusContactCC.setVisibility(View.VISIBLE);
            statusContactCC.setText(R.string.verify_fail_2);
            addMoreVehicle.setVisibility(View.INVISIBLE);
            statusIMG.setImageResource(R.drawable.alert);
            action.setText(R.string.call_customer_care);
            action.setOnClickListener(view -> {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Vehicle Onboarding");
                params.putString("eventAction", "Failed Onboarding");
                params.putString("eventLabel", "Call Customer Care click");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                checkAndRequestCallPermissions(getApplicationContext(), this, REUtils.getREKeys().getCustomerCare(),
                        CALL_PERMISSIONS_REQUESTS, this);
            }  );
            Bundle paramsScr = new Bundle();
            paramsScr.putString("screenname", "Failed Onboarding");
            REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        }else {
            Bundle paramsScr = new Bundle();
            paramsScr.putString("screenname", "Successful Onboarding");
            REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
            addMoreVehicle.setVisibility(View.VISIBLE);
            status.setText(R.string.details_will_be_validated_and_you_will_be_notified_via_email_or_sms);
            statusContactCC.setVisibility(View.GONE);
            statusIMG.setImageResource(R.drawable.success);
            action.setText(R.string.view_my_profile);
            action.setOnClickListener(view -> {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Vehicle Onboarding");
                params.putString("eventAction", "Successful Onboarding");
                params.putString("eventLabel", "View my Profile click");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                REApplication.getInstance().setComingFromVehicleOnboarding(true);
                startActivity(REProfileActivity.getStartIntent(OnboardingResultActivity.this));
                overridePendingTransition(R.anim.slide_up, 0);
                finish();

            });
            addMoreVehicle.setOnClickListener(view -> {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Vehicle Onboarding");
                params.putString("eventAction", "Successful Onboarding");
                params.putString("eventLabel", "Add new vehicle click");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                startActivity(new Intent(OnboardingResultActivity.this, AddVehicleActivity.class));
                finish();
            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        REApplication.getInstance().setComingFromVehicleOnboarding(true);
        startActivity(new Intent(OnboardingResultActivity.this, REHomeActivity.class));
        finish();
    }
}
