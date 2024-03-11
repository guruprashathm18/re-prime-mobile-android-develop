package com.royalenfield.reprime.ui.riderprofile.listeners;



public interface OnLogoutResponseListener {

    void onSuccess();

    void onFailure(String errorMessage);

    void onForgotSuccess();

    void onForgotFailure(String errorMessage);

}
