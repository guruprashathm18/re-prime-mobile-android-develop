package com.royalenfield.reprime.ui.riderprofile.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.web.signup.RequestConsent;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.onboarding.useronboarding.fragment.ConsentFragment;
import com.royalenfield.reprime.ui.onboarding.useronboarding.interactor.UserOnboardingInteractor;
import com.royalenfield.reprime.ui.onboarding.useronboarding.presenter.UserOnboardingPresenter;
import com.royalenfield.reprime.ui.onboarding.useronboarding.views.UserOnboardingView;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

/**
 * This activity is to change the password
 */
public  class ConsentUpdateActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener,
        View.OnClickListener, UserOnboardingView {
private  UserOnboardingPresenter userOnboardingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_update);
        initializeViews();
		userOnboardingPresenter = new UserOnboardingPresenter(this, new UserOnboardingInteractor());
    }

    /**
     * Initializes all the views for the UI....
     */
    private void initializeViews() {
        TitleBarView mTitleBarView = findViewById(R.id.titleBarView_changepassword);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.consent).toUpperCase());
//        mSubmitButton = findViewById(R.id.button_submit);
//        mSubmitButton.setEnabled(false);
//        mSubmitButton.setOnClickListener(this);
//        frame=findViewById(R.id.frame);

loadFragment(new ConsentFragment());

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame, fragment);
        transaction.commit();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
     //   if (v.getId() == R.id.button_submit) {
//            showLoading();


     //   }
    }


	@Override
	public void consentSuccess(String message) {
		REUtils.showErrorDialog(this, message, () -> finish());
	}

	@Override
	public void consentFailure(String error) {
		if(error.equalsIgnoreCase(""))
			error=getResources().getString(R.string.something_went_wrong);
		REUtils.showErrorDialog(this,error);
	}



	public void consentUpdate(@NotNull RequestConsent consentData) {
		userOnboardingPresenter.consentUpdate(consentData);
	}

	@Override
	public void invalidMobileNumber() {

	}

	@Override
	public void onOTPSuccess(@Nullable String reqId) {

	}

	@Override
	public void onOTPFailure(@NonNull String errorMsg) {

	}

	@Override
	public void loginSuccess(@Nullable String requestID) {

	}

	@Override
	public void userNotExist(@Nullable String requestID) {

	}

	@Override
	public void otpVerifyFailure(@NonNull String errorMsg) {

	}

	@Override
	public void otpVerifyFailureInline(@NonNull String errorMsg) {

	}

	@Override
	public void otpInvalidInline(@NonNull String errorMsg) {

	}

	@Override
	public void otpLimitExceed(@NonNull String errorMsg) {

	}

	@Override
	public void otpExpired(@Nullable String errorMsg) {

	}

	@Override
	public void signupSuccess(@Nullable String reqId) {

	}

	@Override
	public void signupFailed(@Nullable String errorMsg) {

	}

	@Override
	public void emailOrMobileExist(@Nullable String errorMsg) {

	}
}
