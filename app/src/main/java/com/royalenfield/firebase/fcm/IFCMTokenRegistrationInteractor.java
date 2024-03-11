package com.royalenfield.firebase.fcm;


import com.royalenfield.reprime.models.request.proxy.firebase.FirebaseTokenRequest;

public interface IFCMTokenRegistrationInteractor {

    void sendNotification(FirebaseTokenRequest firebaseTokenRequest, FCMTokenRegistrationListener onNotificationListener);
}
