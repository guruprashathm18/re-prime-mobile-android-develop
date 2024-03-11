package com.royalenfield.reprime.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.network.listener.INetworkStateListener;
import com.royalenfield.reprime.utils.REUtils;

/**
 * Broadcast Receiver for receiving Network change updates
 */
public class NetworkConnectionReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkConnectionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive");

        switch (action) {
            case INetworkStateListener.CONNECTION_CHANGE:
                if (REUtils.isNetworkAvailable(context)) {
                    Log.d(TAG, "Network Connected");
                    if (REApplication.getInstance().getNetworkChangeListener() != null) {
                        REApplication.getInstance().getNetworkChangeListener().onNetworkConnect();
                    }
                } else {
                    Log.d(TAG, "Network Disconnected");
                    if (REApplication.getInstance().getNetworkChangeListener() != null) {
                        REApplication.getInstance().getNetworkChangeListener().onNetworkDisconnect();
                    }
                }
                break;
        }
    }
}
