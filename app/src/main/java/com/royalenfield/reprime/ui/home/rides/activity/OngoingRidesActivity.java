package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.riderprofile.fragment.ProfileRidesFragment;
import com.royalenfield.reprime.utils.REConstants;

import com.royalenfield.reprime.utils.RELog;

/**
 * Ongoing rides are shown as a list
 */
public class OngoingRidesActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener {

    /**
     *
     */
    private BroadcastReceiver mOngoingBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (REConstants.RIDE_ONGOING_CLOSE.equals(intent.getAction())) {
                closeOngoingRide();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_rides);
        initViews();
        LocalBroadcastManager.getInstance(this).registerReceiver(mOngoingBroadcastReceiver,
                new IntentFilter(REConstants.RIDE_ONGOING_CLOSE));
    }

    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.ongoingride_header);
        mTitleBarView.bindData(this, R.drawable.icon_close, getResources().getString(R.string.text_inride),
                0);
        addRidesFragment();
    }

    /**
     * Adds ongoing rides list
     */
    private void addRidesFragment() {
        try {
            FragmentFrameHolder ridesFrame = findViewById(R.id.fl_ongoingrides);
            ProfileRidesFragment mProfileRidesFragment = ProfileRidesFragment.newInstance();
            Bundle args = new Bundle();
            args.putString(REConstants.RIDECUSTOM_VIEWTYPE, REConstants.ONGOINGRIDE_TYPE);
            mProfileRidesFragment.setArguments(args);
            ridesFrame.loadFragment(this, mProfileRidesFragment, null);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOngoingBroadcastReceiver);
    }

    private void closeOngoingRide() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void onNavigationIconClick() {
        closeOngoingRide();
    }

    @Override
    public void onBackPressed() {
        closeOngoingRide();
    }
}
