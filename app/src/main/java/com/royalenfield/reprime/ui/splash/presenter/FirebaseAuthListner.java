package com.royalenfield.reprime.ui.splash.presenter;

public interface FirebaseAuthListner {
    void onFirebaseAuthSuccess();
    void onFirebaseAuthFailure(Exception exception);
}
