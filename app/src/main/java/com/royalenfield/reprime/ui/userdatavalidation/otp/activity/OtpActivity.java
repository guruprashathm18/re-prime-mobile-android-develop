package com.royalenfield.reprime.ui.userdatavalidation.otp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.royalenfield.firestore.userinfo.ContactDetails;
import com.royalenfield.firestore.userinfo.ProvisioningData;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.activity.DetailsConfirmationActivity;
import com.royalenfield.reprime.ui.userdatavalidation.otp.otpinteractor.OtpInteractor;
import com.royalenfield.reprime.ui.userdatavalidation.otp.presenter.OtpPresenter;
import com.royalenfield.reprime.ui.userdatavalidation.popup.Constants;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.ContactData;
import com.royalenfield.reprime.utils.REUtils;
import com.royalenfield.workmanager.ProvisioningWorker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.ui.userdatavalidation.popup.Constants.KEY_FROM_PROFILE;
import static com.royalenfield.reprime.ui.userdatavalidation.popup.Constants.KEY_PRIMARY_CHECKED;
import static com.royalenfield.reprime.ui.userdatavalidation.popup.Constants.KEY_SECONDARY_ADD_REQ;
import static com.royalenfield.reprime.ui.userdatavalidation.popup.Constants.KEY_SECONDARY_UPDATE_REQ;
import static com.royalenfield.reprime.utils.REConstants.KEY_UPDATE_PROVISIONING;

public class OtpActivity extends REBaseActivity implements IOtpView, OnOtpCompletionListener {

    private static final int TOTAL_TIMER = 60000;
    private static final int INTERVAL = 1000;

    @BindView(R.id.txv_otp_verification_msg)
    TextView mOtpVerificationMsg;

    @BindView(R.id.ll_resend)
    LinearLayout mResendLayout;

    @BindView(R.id.txv_otp_error)
    TextView mOtpError;

    @BindView(R.id.otp_view)
    OtpView mOtpView;

    private OtpPresenter mOtpPresesnter;
    private CountDownTimer mCountDownTimer;
    private TextView mResendText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        mOtpView.requestFocus();
        mResendText = mResendLayout.findViewById(R.id.txv_resend);
        mOtpPresesnter = new OtpPresenter(this, new OtpInteractor());
        initializeView();
        performActionOnIntent();
        startTimer();
        mOtpView.setOtpCompletionListener(this);
    }

    private void performActionOnIntent() {

        if (getIntent() != null && getSecondaryDialingCode() != null) {
            String secondary = getSecondaryDialingCode();
            String str = null;
            if (secondary != null)
                str = secondary.replaceAll("[-+^]*", "");
            if (str != null)
                mOtpPresesnter.sendOTP(str, getSecondaryPhone());
        }

    }

    private boolean isFromEditProfile() {
        if (getIntent() != null)
            return getIntent().getBooleanExtra(KEY_FROM_PROFILE, false);
        else return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startTimer() {
        startCountDownTimer();
    }

    private void startCountDownTimer() {
        mCountDownTimer = new CountDownTimer(TOTAL_TIMER, INTERVAL) {

            @Override
            public void onTick(long l) {
                mResendText.setText(getString(R.string.resend_in_sec, ((int) (l / 1000))));
                mResendText.setTextColor(getResources().getColor(R.color.white_50));
            }

            @Override
            public void onFinish() {
                mResendText.setText(getString(R.string.resend_otp));
                mResendText.setTextColor(getResources().getColor(R.color.white));
            }
        };

        mCountDownTimer.start();
    }


    @OnClick(R.id.ll_resend)
    public void onResendClicked() {

        if (mResendText.getText().equals(getString(R.string.resend_otp))) {
            clearExistingOtpAndError();
            String secondary = getSecondaryDialingCode();
            String str = null;
            if (secondary != null)
                str = secondary.replaceAll("[-+^]*", "");
            if (str != null)
                mOtpPresesnter.sendOTP(str, getSecondaryPhone());
            startTimer();
        }
    }

    private void clearExistingOtpAndError() {
        mOtpView.setText("");
        mOtpError.setVisibility(View.GONE);
    }

    private String getSecondaryPhone() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(Constants.KEY_SECONDARY_PHONE_NUMBER);
        }
        return null;
    }

    public String getEmail() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(Constants.KEY_EMAIL);
        }
        return null;
    }


    private String getSecondaryDialingCode() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(Constants.KEY_SECONDARY_COUNTRY_CODE);
        }
        return null;
    }

    private String getEmergencyPhone() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(Constants.KEY_EMERGENCY_PHONE_NUMBER);
        }
        return null;
    }

    private String getEmergencyDialingCode() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(Constants.KEY_EMERGENCY_COUNTRY_CODE);
        }
        return null;
    }

    private void initializeView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mOtpVerificationMsg.setText(getString(R.string.please_enter_verification_code
                , getSecondaryDialingCode(), getSecondaryPhone()));
        mOtpView.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                mOtpError.setVisibility(View.GONE);
            }
            return false;
        });
    }

    @Override
    public void onOtpSubmitted() {
        mOtpPresesnter.updateUserDataValidationFlag();
        //creating a data object
        //to pass the data with workRequest
        //we can put as many variables needed
        List<String> list=REUtils.getConnectedBike(REApplication.getAppContext());
        if(list.size()>0) {
            try {
                REPreference.getInstance().putBoolean(REApplication.getAppContext(), KEY_UPDATE_PROVISIONING, true);
            } catch (PreferenceException e) {
                e.printStackTrace();
            }
            ProvisioningData provisioningData = new ProvisioningData();
            ProvisioningData.EmergencyNoDetails emergencyNoDetails = new ProvisioningData.EmergencyNoDetails();

            emergencyNoDetails.setCallingCode(getEmergencyDialingCode().replaceAll("[-+^]*", ""));
            emergencyNoDetails.setEmergencyNo(getEmergencyPhone().replaceAll("[-+^]*", ""));
            ProvisioningData.PrimaryNoDetails primaryNoDetails = new ProvisioningData.PrimaryNoDetails();
            primaryNoDetails.setCallingCode(getSecondaryDialingCode().replaceAll("[-+^]*", ""));
            primaryNoDetails.setPrimaryNo(getSecondaryPhone().replaceAll("[-+^]*", ""));
            provisioningData.setNewPrimaryNoDetails(primaryNoDetails);
            provisioningData.setNewEmergencyNoDetails(emergencyNoDetails);
            provisioningData.setBikesOwned(list);
            String data = new Gson().toJson(provisioningData);
            REUtils.startWorker(data);
        }
        showConfirmationScreen();
    }



    @Override
    public void showInlineError(String message) {
        mOtpError.setText(message);
        mOtpError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSendOtpFail(String errorMessage) {
        showErrorDialog(this, errorMessage);
    }

    public void showErrorDialog(final Context context, String message) {
        try {
            TextView alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(v -> {
                dialog.dismiss();
                mCountDownTimer.cancel();
                mResendText.setText(getString(R.string.resend_otp));
                mResendText.setTextColor(getResources().getColor(R.color.white));
            });
        } catch (Exception e) {
            RELog.e(e);
        }

    }


    public void showConfirmationScreen() {
        if (!isFromEditProfile()) {
            Intent intent = new Intent(OtpActivity.this, DetailsConfirmationActivity.class);
            intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_TITLE, getString(R.string.details_validation_title));
            intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_MESSAGE, "You already have "
                    + REServiceSharedPreference.getVehicleData(this).size()
                    + " Motorcycle linked to your mobile number");
            intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_Button, getString(R.string.show_motorcycle));
            intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_TYPE, getString(R.string.pop_up_validation_confirmation));
            startActivity(intent);
        }
        setResult(2);
        finish();
    }

    @Override
    public void showError(String message) {
        showErrorDialog(this, message);
    }

    @Override
    public void saveFCMTokenOnServer() {
        //call base activity method
        try {
            saveTokenOnServer();
        } catch (PreferenceException e) {
            RELog.e(e);
        }
    }

    @OnClick(R.id.cancel)
    public void onCancelClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    public void onOtpCompleted(String otp) {
        String secondary = getSecondaryDialingCode();
        String str = null;
        if (secondary != null)
            str = secondary.replaceAll("[-+^]*", "");
        REUserModelStore.getInstance().setEmail(getEmail());
        REUserModelStore.getInstance().getProfileData().getContactDetails().setEmail(getEmail());
        mOtpPresesnter.submitOtp(getSecondaryPhone(), otp,str,getEmail());
    }

    @Override
    public void onOTPSuccess() {
        showToastMessage(getResources().getString(R.string.text_message_otp_sent_success));
    }
}
