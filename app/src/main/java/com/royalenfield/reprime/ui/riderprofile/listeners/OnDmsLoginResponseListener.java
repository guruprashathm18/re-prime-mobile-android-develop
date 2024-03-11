package com.royalenfield.reprime.ui.riderprofile.listeners;

/**
 * @author BOP1KOR on 12/28/2018.
 */

public interface OnDmsLoginResponseListener {

    void onDmsLoginSuccess();

    void onDmsLoginFailure(String errorMessage);
}
