package com.royalenfield.reprime.ui.home.service.rsa;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import com.royalenfield.reprime.ui.home.service.rsa.listner.IRETelephoneManager;
import com.royalenfield.reprime.application.REApplication;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * class includes method to check phone state like sim card is present or not and
 * startCallIntent to start ACTION_CALL intent.
 */
public class RETelephonyManager {


    public boolean isTelephonyEnabled() {
        TelephonyManager mTelephonyManager = (TelephonyManager) REApplication.getInstance().getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        if (mTelephonyManager != null) {
            return mTelephonyManager.getSimState() ==
                    TelephonyManager.SIM_STATE_READY;
        }
        return false;
    }

    public void startCallIntent(Activity mRidesAndServiceActivity, IRETelephoneManager mIRETelephoneManager, String mPhoneNo) {
        if (isTelephonyEnabled()) {
            try {
                String callingNumber;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                if (mPhoneNo.contains("-")) {
                    callingNumber = mPhoneNo.split("-")[1];
                    callIntent.setData(Uri.parse("tel:" + callingNumber));
                } else {
                    callingNumber = mPhoneNo;
                    callIntent.setData(Uri.parse("tel:" + callingNumber));
                }

                if (ActivityCompat.checkSelfPermission(mRidesAndServiceActivity,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mRidesAndServiceActivity.startActivity(callIntent);
            } catch (Exception e) {
                mIRETelephoneManager.simError("Error");
                return;
            }
        } else {
            mIRETelephoneManager.simError("Sim is not present");
        }

    }

}
