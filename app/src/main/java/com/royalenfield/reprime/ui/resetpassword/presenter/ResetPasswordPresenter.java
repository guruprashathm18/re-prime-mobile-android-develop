package com.royalenfield.reprime.ui.resetpassword.presenter;

import com.royalenfield.reprime.ui.resetpassword.views.ResetPasswordView;
import com.royalenfield.reprime.ui.resetpassword.interactor.ResetPasswordInteractor;
import com.royalenfield.reprime.ui.resetpassword.listeners.OnResetPasswordResponseListener;
import com.royalenfield.reprime.utils.REUtils;

/**
 * @author BOP1KOR on 12/19/2018.
 * <p>
 * The {@link ResetPasswordPresenter} is responsible to act as the middle man between {@link ResetPasswordView} and Model.
 * It retrieves data from the Model and returns it formatted to the View.
 * But unlike the typical MVC, it also decides what happens when you interact with the View.
 */

public class ResetPasswordPresenter implements OnResetPasswordResponseListener, IResetPasswordPresenter {
    private static final String TAG = ResetPasswordPresenter.class.getSimpleName();

    private ResetPasswordView mResetPasswordView;
    private ResetPasswordInteractor mResetPasswordInteractor;

    public ResetPasswordPresenter(ResetPasswordView resetPasswordView, ResetPasswordInteractor resetPasswordInteractor) {
        this.mResetPasswordView = resetPasswordView;
        this.mResetPasswordInteractor = resetPasswordInteractor;
    }

    @Override
    public void resetPassword(String userID, String newPassword, String confirmPassword, String otp) {
        //show when all fields are empty
        if (otp.isEmpty() && newPassword.isEmpty() && confirmPassword.isEmpty()) {
            mResetPasswordView.onAllEmptyFields();
            return;
        }

        //validate otp field should not be empty...
        if (otp.isEmpty()) {
            mResetPasswordView.onOtpEmpty();
            return;
        }

        //validate new password field should not be empty.....
        if (newPassword.isEmpty()) {
            mResetPasswordView.onNewPasswordEmpty();
            return;
        }
        //validate confirm password field should not be empty...
        if (confirmPassword.isEmpty()) {
            mResetPasswordView.onConfirmPasswordEmpty();
            return;
        }
        if(!REUtils.isValidPassword(newPassword)){
            mResetPasswordView.onNewPasswordInvalid();
            return;
        }
        if(!REUtils.isValidPassword(confirmPassword)){
            mResetPasswordView.onConfirmPasswordInvalid();
            return;
        }

        //New password and confirm password both field should match
        if (!newPassword.equals(confirmPassword)) {
            mResetPasswordView.onPasswordConfirmationError();
            return;
        }
        //Shows the progress bar
        if (mResetPasswordView != null) {
            mResetPasswordView.showLoading();
        }

        mResetPasswordInteractor.resetPassword(userID, newPassword, otp, this);

    }


    @Override
    public void onSuccess() {
        if (mResetPasswordView != null) {
            mResetPasswordView.hideLoading();
            mResetPasswordView.onResetSuccess();
        }

    }

    @Override
    public void onFailure(String errorMessage) {
        if (mResetPasswordView != null) {
            mResetPasswordView.hideLoading();
            mResetPasswordView.onResetFailure(errorMessage);
        }

    }
}
