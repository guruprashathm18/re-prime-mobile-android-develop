package com.royalenfield.firebase.fireStore;

public interface OnFirestoreUserSettingCallback {

    void onFirestoreUserSettingSuccess();
    void onFirestoreUserSettingFailure(String message);
}
