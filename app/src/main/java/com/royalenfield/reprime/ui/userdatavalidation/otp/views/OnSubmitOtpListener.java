package com.royalenfield.reprime.ui.userdatavalidation.otp.views;

public interface OnSubmitOtpListener {

    void onSubmitSuccess();

    void onSubmitFail(String message);

    void onSwapSuccess();

    void onSwapFail(String message);
}
