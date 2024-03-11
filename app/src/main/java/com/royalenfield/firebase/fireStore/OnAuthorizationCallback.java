package com.royalenfield.firebase.fireStore;

public interface OnAuthorizationCallback {

    void onAuthorizeSuccess(String deviceIMei, boolean isConsentTaken);

    void onAuthorizeFailure(String message);

}
