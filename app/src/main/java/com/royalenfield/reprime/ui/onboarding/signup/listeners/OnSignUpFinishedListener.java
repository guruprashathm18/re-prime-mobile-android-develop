package com.royalenfield.reprime.ui.onboarding.signup.listeners;

public interface OnSignUpFinishedListener {

    void onSuccess();

    void onFailure(String errorMessage);
}
