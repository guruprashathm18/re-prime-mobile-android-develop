package com.royalenfield.reprime.notification.listener;

/**
 * @author Created by kiran on 16/04/20.
 */
public interface PhoneStateListener {

    void onIncomingCall();

    void onCallAccepted();

    void onCallRejected();

    void onCallEnded();

    void onCallMissed();
}
