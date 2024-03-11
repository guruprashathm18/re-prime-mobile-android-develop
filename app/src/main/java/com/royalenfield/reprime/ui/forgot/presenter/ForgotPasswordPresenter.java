package com.royalenfield.reprime.ui.forgot.presenter;

import android.util.Log;
import com.royalenfield.reprime.ui.forgot.interactor.ForgotPasswordInteractor;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.ui.forgot.views.ForgotPasswordView;
import com.royalenfield.reprime.utils.REUtils;

/**
 * @author BOP1KOR on 12/19/2018.
 * The {@link ForgotPasswordPresenter} is responsible to act as the middle man between {@link ForgotPasswordView} and Model.
 * It retrieves data from the Model and returns it formatted to the View.
 * But unlike the typical MVC, it also decides what happens when you interact with the View.
 */

public class ForgotPasswordPresenter implements OnSendOtpResponseListener, IForgotPasswordPresenter {
    private static final String TAG = ForgotPasswordPresenter.class.getSimpleName();

    private ForgotPasswordView mForgotPasswordView;
    private ForgotPasswordInteractor mForgotPasswordInteractor;

    public ForgotPasswordPresenter(ForgotPasswordView mForgotPasswordView, ForgotPasswordInteractor mForgotPasswordInteractor) {
        this.mForgotPasswordView = mForgotPasswordView;
        this.mForgotPasswordInteractor = mForgotPasswordInteractor;
    }

    @Override
    public void sendOtp(String userID) {
        //validate field should not be empty......
        if (userID == null || userID.isEmpty()) {
            mForgotPasswordView.onEmailOrPhoneNumberEmpty();
            return;
        }

        //Validate it is valid user id......
        if (REUtils.isMobileNumber(userID)) {
            if (!REUtils.isValidMobileNumber(userID)) {
                Log.d(TAG, "Invalid mobile number");
                mForgotPasswordView.onInvalidPhoneNumber();
                return;
            }
            //Validate email ID......
        } else if (!REUtils.isValidEmailId(userID)) {
            Log.d(TAG, "Not a valid email Id");
            mForgotPasswordView.onInvalidEmailId();
            return;
        }

        //Show progress bar.
        if (mForgotPasswordView != null) {
            mForgotPasswordView.showLoading();
        }

        mForgotPasswordInteractor.sendOtp(userID, this);

    }

    @Override
    public void onDestroy() {
        mForgotPasswordView = null;
    }


    @Override
    public void onSuccess() {
        if (mForgotPasswordView != null) {
            mForgotPasswordView.hideLoading();
            mForgotPasswordView.onOtpSendSuccess();
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if (mForgotPasswordView != null) {
            mForgotPasswordView.hideLoading();
            mForgotPasswordView.onOtpSendFailure(errorMessage);
        }
    }

    @Override
    public void onInlineErrorVisibility(String userID) {
        if (userID.length() >= 1) {
            mForgotPasswordView.hideInlineError();
        }
    }
}
