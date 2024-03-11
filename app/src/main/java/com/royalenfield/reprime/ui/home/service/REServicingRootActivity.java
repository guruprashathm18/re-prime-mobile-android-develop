package com.royalenfield.reprime.ui.home.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.service.fragment.REServicingFragment;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class REServicingRootActivity extends REBaseActivity implements View.OnClickListener {


    private OnPushNotificationUpdateListener onPushNotificationUpdateListener;


    public OnPushNotificationUpdateListener getPushNotificationUpdateListener() {
        return onPushNotificationUpdateListener;
    }

    public void setPushNotificationUpdateListener(OnPushNotificationUpdateListener fragmentRefreshListener) {
        this.onPushNotificationUpdateListener = fragmentRefreshListener;
    }

    /**
     * Listener for the updating the service jobCart status by PushNotification.
     */
    public interface OnPushNotificationUpdateListener {
        void updateJobCartStatus(String jobCartStatus, String regNo);
    }

    /**
     *
     */
    BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (REConstants.PUSH_NOTIFICATION.equals(intent.getAction())) {
                // new push notification is received
                Log.d("test_onrecieve", "onReceive Called");
                String jobCartStatus = intent.getStringExtra("jobCardStatus");
                String mRegistrationNumber = intent.getStringExtra("regNo");
                if (onPushNotificationUpdateListener != null) {
                    onPushNotificationUpdateListener.updateJobCartStatus(jobCartStatus, mRegistrationNumber);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_servicing_root);
        initViews();
        enableTheLocation();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REConstants.PUSH_NOTIFICATION));
    }

    private void initViews() {
        TextView tvHeader = findViewById(R.id.tv_actionbar_title);
        tvHeader.setText(getString(R.string.servicing_text));

        ImageView ivNavigation = findViewById(R.id.iv_navigation);
        ivNavigation.setImageResource(R.drawable.back_arrow);
        ivNavigation.setOnClickListener(this);
        findViewById(R.id.iv_title_profile_header_logo).setOnClickListener(this);
    }

    public void loadFragment() {
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Service");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.root_frame, new REServicingFragment());
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_navigation) {
            REApplication.getInstance().setServiceBookingResponse(null);
            REApplication.getInstance().setDescriptionValue("");
            try {
                REPreference.getInstance().putString(this, "address", "");
            } catch (PreferenceException e) {
                RELog.e(e);
            }
            finish();
            overridePendingTransition(0, R.anim.slide_out_right);
        } else if (view.getId() == R.id.iv_title_profile_header_logo) {
            REApplication.getInstance().setComingFromVehicleOnboarding(true);
            Intent intent = new Intent(this, REHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister the broad cast reciever
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        REApplication.getInstance().setServiceBookingResponse(null);
        REApplication.getInstance().setDescriptionValue("");
        try {
            REPreference.getInstance().putString(this, "address", "");
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        super.onBackPressed();
    }


}
