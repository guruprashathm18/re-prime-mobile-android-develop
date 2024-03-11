package com.royalenfield.reprime.ui.resetpassword.presenter;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public interface IResetPasswordPresenter {

    /**
     * API call for resetting the user login password by verifying the OTP.
     *
     * @param userID          user entered id phone number or email id.
     * @param newPassword     new entered password.
     * @param confirmPassword confirm password.
     * @param otp             generated otp.
     */
    void resetPassword(String userID, String newPassword, String confirmPassword, String otp);

}
