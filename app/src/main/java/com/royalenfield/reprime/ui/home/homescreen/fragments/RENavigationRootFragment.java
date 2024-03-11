package com.royalenfield.reprime.ui.home.homescreen.fragments;

import static com.royalenfield.reprime.utils.REConstants.ALERT_MESSAGE;
import static com.royalenfield.reprime.utils.REConstants.ALERT_TYPE;
import static com.royalenfield.reprime.utils.REConstants.ALERT_TYPE_NAVIGATION_LOGIN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.royalenfield.bluetooth.ble.BLEModel;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.REAlertFragment;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;

public class RENavigationRootFragment extends REBaseFragment {

    private FragmentTransaction mFragmentTransaction;
    private TextView mTvHeading;
    private static RENavigationRootFragment instance = null;
    public static RENavigationRootFragment newInstance() {
        return new RENavigationRootFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance = this;
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_navigation_root, container, false);
        mTvHeading = view.findViewById(R.id.tv_actionbar_title);
        mTvHeading.setText(getString(R.string.navigation_title));
        if (mFragmentTransaction == null) {
            assert getFragmentManager() != null;
            mFragmentTransaction = getFragmentManager()
                    .beginTransaction();
            if (REUtils.isUserLoggedIn()) {
                mFragmentTransaction.replace(R.id.root_navigation_frame, new RENavigationFragment(), "RENavigationFragment");
            } else {
                REAlertFragment alertFragment = new REAlertFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ALERT_MESSAGE, getString(R.string.text_alert_login_message));
                bundle.putString(ALERT_TYPE, ALERT_TYPE_NAVIGATION_LOGIN);
                alertFragment.setArguments(bundle);
                mFragmentTransaction.replace(R.id.root_navigation_frame, alertFragment);
            }
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commitAllowingStateLoss();
        }
        return view;
    }

    public static RENavigationRootFragment getInstance() {
        return instance;
    }
    public void showorHideTitle (boolean value){
        mTvHeading.setVisibility(value ? View.GONE : View.VISIBLE);
    }
    @Override
    public void onDestroy() {
        mFragmentTransaction = null;
        REApplication.getInstance().setmConnectedDeviceInfo(new ArrayList<>());
        REApplication.getInstance().setIsDeviceAuthorised(false);
        if (BLEModel.getInstance().getBluetoothGatt() != null) {
            BLEModel.getInstance().getBluetoothGatt().disconnect();
        }
        super.onDestroy();
    }
}