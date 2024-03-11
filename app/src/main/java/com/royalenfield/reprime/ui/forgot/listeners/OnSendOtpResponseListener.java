package com.royalenfield.reprime.ui.forgot.listeners;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public interface OnSendOtpResponseListener {

    void onSuccess();

    void onFailure(String errorMessage);
}
