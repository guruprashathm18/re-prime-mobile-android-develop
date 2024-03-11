package com.royalenfield.firebase.fireStore;

public interface OnFirestoreCallback {

    void onFirestoreSuccess();
    void onFirestoreFailure(String message);
}
