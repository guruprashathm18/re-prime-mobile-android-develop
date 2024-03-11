package com.royalenfield.reprime.ui.splash.presenter;

public interface FirebaseConfigListner {
    void onRemoteConfigSuccess();
    void onRemoteConfigFailure(String msg);
}
