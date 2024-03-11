package com.royalenfield.reprime.ui.userdatavalidation.otp.activity;

import com.royalenfield.reprime.preference.PreferenceException;

public interface IOtpView {
    void onOtpSubmitted();

    void showInlineError(String error);

    void onSendOtpFail(String errorMessage);

    void showConfirmationScreen();

    void showError(String message);

    void saveFCMTokenOnServer();

    void onOTPSuccess();
}
