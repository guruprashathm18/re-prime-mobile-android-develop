package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.activity;

import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor.AddVehicleInteractor;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor.UploadDocInteractor;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners.OnDocsSubmitListener;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.presenter.AddVehiclePresenter;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.views.AddVehicleView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;


public class AddVehicleActivity extends REBaseActivity implements View.OnClickListener, AddVehicleView
        , TextWatcher, OnDocsSubmitListener {

    private EditText chassisNumber, registrationNumber;
    private ImageButton verify;
    private AddVehiclePresenter addVehiclePresenter;
    private TextView errorRegistrationNumber,errorChassisNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        initViews();
        addVehiclePresenter = new AddVehiclePresenter(this, new AddVehicleInteractor());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            REApplication.getInstance().setComingFromVehicleOnboarding(true);
            startActivity(new Intent(this, REHomeActivity.class));
            finish();
        }
        return true;
    }

    private void initViews() {
        chassisNumber = findViewById(R.id.chassis_number);
        chassisNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideChassisNumberError();
                if (s.toString().trim().length() > 0) {
                    enableVerifyButton();
                } else {
                    disableVerifyButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registrationNumber = findViewById(R.id.registration_number);
        registrationNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideRegistrationNumberError();
                if (s.toString().trim().length() > 0) {
                    enableVerifyButton();
                } else {
                 disableVerifyButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify = findViewById(R.id.verify_vehicle);
        errorRegistrationNumber = findViewById(R.id.error_registration_number);
        errorChassisNumber = findViewById(R.id.error_chassis_number);
        verify.setOnClickListener(this);
        verify.setEnabled(false);
        verify.setClickable(false);
        verify.setBackgroundResource(R.drawable.disable_verify_button);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.verify_vehicle) {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Vehicle Onboarding");
            params.putString("eventAction", "Verify click");
           REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
            if (addVehiclePresenter.validateAddVehicleDetails(chassisNumber.getText().toString().trim(), registrationNumber.getText().toString().trim(), true)) {
                showLoading();
                addVehiclePresenter.validateVehicleDetail(chassisNumber.getText().toString().trim()
                        , registrationNumber.getText().toString().trim());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Add Vehicle");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }

    @Override
    public void onPurchaseDateSelected(String date) {
        chassisNumber.setText(date);
    }

    @Override
    public void enableVerifyButton() {
      //  errorChassisNumber.setVisibility(View.GONE);
       // errorRegistrationNumber.setVisibility(View.GONE);
        if(errorChassisNumber.getVisibility()==View.GONE&&errorRegistrationNumber.getVisibility()==View.GONE) {
            verify.setEnabled(true);
            verify.setClickable(true);
            verify.setBackgroundResource(R.drawable.enable_verify_button);
        }
    }

    @Override
    public void disableVerifyButton() {
        if(chassisNumber.getText().toString().trim().length()==0&&registrationNumber.getText().toString().trim().length()==0) {
            verify.setEnabled(false);
            verify.setClickable(false);
            verify.setBackgroundResource(R.drawable.disable_verify_button);
        }
    }

    @Override
    public void onSuccessResponse(String code, String message, AddVehicleResponse addVehicleResponse) {
        hideLoading();
        switch (code) {
            case "200":
                if (addVehicleResponse!=null&&addVehicleResponse.getData()!=null&&addVehicleResponse.getData().isVehiclePurchaseDateLessThan30Days()) {
                    new UploadDocInteractor().uploadDocs(null, this,addVehicleResponse);
                } else {
                    if(addVehicleResponse!=null){
                        Intent intent=new Intent(this, UploadDocumentActivity.class);
                        Gson gson = new Gson();
                        String addVehicleResponseJson = gson.toJson(addVehicleResponse);
                        intent.putExtra("addVehicleResponseJson", addVehicleResponseJson);
                        startActivity(intent);
                        finish();
                    }else {
                        REUtils.showErrorDialogVO(this,getResources().getString(R.string.something_went_wrong));
                    }

                }
                break;
            case "1028":
                hideLoading();
                Intent intent = new Intent(this, OnboardingResultActivity.class);
                intent.putExtra("sender", "verifyMotorCycle");
                startActivity(intent);
                finish();
                break;
            default:
                REUtils.showErrorDialogVO(this,message!=null?message:getResources().getString(R.string.something_went_wrong));
                break;

        }

    }

    @Override
    public void onnInvalidChassisNumber() {
        errorChassisNumber.setVisibility(View.VISIBLE);
    }

    @Override
    public void onnInvalidRegistrationNumber() {
        errorRegistrationNumber.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailureResponse(String message) {
        hideLoading();
        Intent intent = new Intent(this, OnboardingResultActivity.class);
        intent.putExtra("sender", "verifyMotorCycle");
        startActivity(intent);
        finish();
    }

    @Override
    public void hideRegistrationNumberError() {
        errorRegistrationNumber.setVisibility(View.GONE);
    }

    @Override
    public void hideChassisNumberError() {
        errorChassisNumber.setVisibility(View.GONE);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       // addVehiclePresenter.validateAddVehicleDetails(chassisNumber.getText().toString().trim(), registrationNumber.getText().toString().trim(),false);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    protected void onDestroy() {
        addVehiclePresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        REApplication.getInstance().setComingFromVehicleOnboarding(true);
        startActivity(new Intent(this, REHomeActivity.class));
    }

    @Override
    public void onSuccess() {
        REApplication.getInstance().setComingFromVehicleOnboarding(true);
        startActivity(new Intent(AddVehicleActivity.this, REHomeActivity.class));
        REUtils.showErrorDialog(this, getResources().getString(R.string.thirty_days_vehicle_sucess));
        finish();
    }

    @Override
    public void onFailure(String errorMessage) {
        hideLoading();
        Intent intent = new Intent(this, OnboardingResultActivity.class);
        intent.putExtra("sender", "verifyMotorCycle");
        startActivity(intent);
        finish();
    }
}
