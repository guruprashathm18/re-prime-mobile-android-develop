package com.royalenfield.firebase.fcm;

public interface FCMTokenRegistrationListener {

    void onFCMTokenSentSuccess();

    void onFCMTokenSentFailure(String errorMessage);
}
