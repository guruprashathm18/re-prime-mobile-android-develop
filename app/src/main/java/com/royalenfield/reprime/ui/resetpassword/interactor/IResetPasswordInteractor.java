package com.royalenfield.reprime.ui.resetpassword.interactor;

import com.royalenfield.reprime.ui.resetpassword.listeners.OnResetPasswordResponseListener;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public interface IResetPasswordInteractor {

    /**
     * Reset the user password using the OTP.
     *
     * @param userId                    user id.
     * @param password                  new password.
     * @param otp                       otp.
     * @param onSendOtpResponseListener response callback to update the presenter.
     */
    void resetPassword(String userId, String password, String otp, OnResetPasswordResponseListener onSendOtpResponseListener);
}
