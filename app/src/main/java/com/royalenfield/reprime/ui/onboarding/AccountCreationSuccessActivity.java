package com.royalenfield.reprime.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.rides.activity.InRideActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.ui.riderprofile.interactor.LogoutInteractor;
import com.royalenfield.reprime.ui.riderprofile.presenter.LogoutPresenter;
import com.royalenfield.reprime.ui.riderprofile.views.LogoutView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_NAVIGATION_FROM;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class AccountCreationSuccessActivity extends REBaseActivity implements View.OnClickListener, LogoutView {

    private Handler handler = new Handler();
    private Runnable runnable;
    private String mType;
    private String mPhoneNumber;
    private boolean isFromUpdateProfile;
    private LogoutPresenter mLogoutPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getIntentData();
            screenTimeout();
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void getIntentData() {
        mType = getIntent().getStringExtra(REConstants.SUCCESS_ACTIVITY);
        isFromUpdateProfile = getIntent().getBooleanExtra(KEY_NAVIGATION_FROM, false);
        mPhoneNumber = getIntent().getStringExtra(REConstants.CREATOR_PHONE_NO);
        mLogoutPresenter = new LogoutPresenter(this, new LogoutInteractor());
        if (mType.equals(REConstants.PASTRIDE_TYPE) || mType.equals(REConstants.PUBLISH_RIDE)) {
            setContentView(R.layout.activity_ride_status);
        } else {
            setContentView(R.layout.activity_done_successfully);
            initViews();
        }
    }

    private void initViews() {
        TextView textView_message = findViewById(R.id.textview_message);
        ImageView imageView_close = findViewById(R.id.imageView_close);
		TextView textViewsub_message = findViewById(R.id.textview_sub_message);
        if (mType.equals(REConstants.USER_CREATED_JOIN_RIDE)) {
            textView_message.setText(R.string.user_created_ride_join);
            imageView_close.setVisibility(View.VISIBLE);
            ImageView imageRoad = findViewById(R.id.image_road);
            imageRoad.setVisibility(View.GONE);
            ImageView imageBike = findViewById(R.id.image_bike);
            imageBike.setVisibility(View.GONE);
            TextView tvContactRider = findViewById(R.id.tv_contact_rider);
            tvContactRider.setVisibility(View.VISIBLE);
            TextView tvRiderPhNo = findViewById(R.id.tv_rider_ph_no);
            tvRiderPhNo.setVisibility(View.VISIBLE);
            SpannableString phNoUnderline = new SpannableString(mPhoneNumber);
            phNoUnderline.setSpan(new UnderlineSpan(), 0, mPhoneNumber.length(), 0);
            tvRiderPhNo.setText(phNoUnderline);
        } else if (mType.equalsIgnoreCase(REConstants.UPCOMIMGRIDE_TYPE)) {
            textView_message.setText(R.string.start_now_splash_text);
        } else if (mType.equalsIgnoreCase(REConstants.ENDRIDE_TYPE)) {
            textView_message.setText(R.string.end_now_splash_text);
        } else if (mType.equalsIgnoreCase(REConstants.VERIFY_ACCOUNT)) {
            textView_message.setText(R.string.successfully_registered);
        } else if (mType.equalsIgnoreCase(REConstants.BLE_DEVICE_PAIRED)) {
            try {
                REPreference.getInstance().putBoolean(this, "isTripperUnpaired", false);
            } catch (PreferenceException e) {
                e.printStackTrace();
            }
            textView_message.setText(R.string.text_ble_device_paired);
        }
        else if (mType.equalsIgnoreCase(REConstants.AUTHORIZED_ACTIVITY)) {
//			Bundle paramsScr = new Bundle();
//			paramsScr.putString("screenname", "Connection Paired Successful");
//			REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
			textViewsub_message.setVisibility(View.VISIBLE);
            textView_message.setText(R.string.successfully_authorized);
        }
        else {
            textView_message.setText(R.string.account_created_successfully);
        }

        if (isFromUpdateProfile) {
            textView_message.setText(R.string.mobile_updated_success);
        }
        imageView_close.setOnClickListener(this);
    }

    /**
     * Screen to show account creation success.
     */
    private void screenTimeout() {
        int SPLASH_TIMEOUT = 3000;
        // Handler for opening RidesAndServiceActivity
        runnable = () -> {
            if (isFromUpdateProfile) {
                if (REUtils.UPDATED_EMAIL.equalsIgnoreCase(REUserModelStore.getInstance().getEmail()) && REUtils.UPDATED_MOBILE.equalsIgnoreCase(REUserModelStore.getInstance().getPhoneNo()) && REUtils.UPDATED_COUNTRY_CODE.equalsIgnoreCase(REUserModelStore.getInstance().getCallingCde().replaceAll("[-+^]*", ""))) {
                    if (mType.equals(REConstants.VERIFY_ACCOUNT) && getIntent().getIntExtra(PCUtils.PC_CALLING_ACTIVITY, 0) == LoginActivity.CODE_PC_ACTIVITY) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (mType.equals(REConstants.VERIFY_ACCOUNT) && getIntent().getIntExtra(REUtils.NAVIGATION_CALLING_ACTIVITY, 0) == LoginActivity.CODE_NAVIGATION_SCREEN) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), REHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                        finish();
                    }
                } else {
                    mLogoutPresenter.logout();
                }
            } else if (mType.equals(REConstants.UPCOMIMGRIDE_TYPE)) {
                try {
                    Intent intent = new Intent(getApplicationContext(), InRideActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                    finish();
                } catch (Exception e) {
                    RELog.e(e);
                }
            } else if (mType.equals(REConstants.PUBLISH_RIDE)) {
                Intent intent = new Intent(getApplicationContext(), REProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            } else if (mType.equals(REConstants.USER_CREATED_JOIN_RIDE) || mType.equals(REConstants.ENDRIDE_TYPE)) {
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                finish();
            } else if (mType.equals(REConstants.VERIFY_ACCOUNT) && getIntent().getIntExtra(PCUtils.PC_CALLING_ACTIVITY, 0) == LoginActivity.CODE_PC_ACTIVITY) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else if (mType.equalsIgnoreCase(REConstants.BLE_DEVICE_PAIRED)) {
                finish();
            }
            else if (mType.equalsIgnoreCase(REConstants.AUTHORIZED_ACTIVITY)) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
            else {
                Intent intent = new Intent(getApplicationContext(), REHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                finish();
            }
        };
        handler.postDelayed(runnable, SPLASH_TIMEOUT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageView_close) {
            startActivity(new Intent(getApplicationContext(), REHomeActivity.class));
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
            finish();
        }
    }

    //Disable back press
    @Override
    public void onBackPressed() {

    }

    @Override
    public void onLogoutSuccess() {
        gotoSignin();
    }

    @Override
    public void onLogoutFailure(String errorMessage) {
        // REUtils.showErrorDialog(this, errorMessage);
        gotoSignin();
    }

    public void gotoSignin() {
        REUtils.CHECK_VEHICLE_PENDING = false;
		REUtils.CHECK_VEHICLE_SYNCING_FAILED = false;
        FirestoreManager.getInstance().removeFirestoreEvents();
        try {
            REPreference.getInstance().removeAll(getApplicationContext());
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        REUtils.setFCMTokenSent(false);
        REUtils.navigateToSplash();
    }

    @Override
    public void onForgotSuccess() {

    }

    @Override
    public void onForgotFailure(String errorMessage) {

    }
}
