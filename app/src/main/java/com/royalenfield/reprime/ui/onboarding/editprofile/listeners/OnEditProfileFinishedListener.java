package com.royalenfield.reprime.ui.onboarding.editprofile.listeners;

public interface OnEditProfileFinishedListener {

    void onUpdateSuccess();

    void onUpdateFailure(String errorMessage);

    void onGetProfileDetailsSuccess();

    void onGetProfileDetailsFailure(String errorCode);

}
