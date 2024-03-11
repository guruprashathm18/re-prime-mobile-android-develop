package com.royalenfield.reprime.notification.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.royalenfield.reprime.application.REApplication;

/**
 * @author Created by kiran on 16/04/20.
 */
public class PhoneStateReceiver extends BroadcastReceiver {

    private static final String TAG = PhoneStateReceiver.class.getSimpleName();
    private REApplication mApplication = REApplication.getInstance();

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d(TAG, "Call Receiver fired");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            assert state != null;
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.d(TAG, "Incoming Call State");
                mApplication.setIncomingCall(true);
                if (null != mApplication.getPhoneStateListener())
                    mApplication.getPhoneStateListener().onIncomingCall();
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                Log.d(TAG, "Call Answered State");
                mApplication.setCallAccepted(true);
                if (null != mApplication.getPhoneStateListener())
                    mApplication.getPhoneStateListener().onCallAccepted();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if (mApplication.isIncomingCall() && !mApplication.isCallAccepted()) {
                    //Now it's a missed call
                    Log.d(TAG, "Call Missed State");
                    if (null != mApplication.getPhoneStateListener())
                        mApplication.getPhoneStateListener().onCallMissed();

                } else if (mApplication.isIncomingCall()) {
                    //Ended or Rejected by the user
                    Log.d(TAG, "Call Ended/Rejected State");
                    if (null != mApplication.getPhoneStateListener())
                        mApplication.getPhoneStateListener().onCallEnded();
                }
                mApplication.setIncomingCall(false);
                mApplication.setCallAccepted(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


