package com.royalenfield.reprime.ui.home.navigation.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.configuration.ConfigurationPrefManager;
import com.royalenfield.reprime.ui.riderprofile.activity.AppSettingActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.HashMap;

public class NavigationFilterScreen extends BottomSheetDialogFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
    //private SwitchCompat mSwitchTraffic;
    private SwitchCompat mSwitchHighway;
    private SwitchCompat mSwitchToll;
    private SwitchCompat mSwitchFerry;
    private SwitchCompat mSwitchETADistance;
    private SwitchCompat mSwitchNightMode;
    private SwitchCompat mSwitchCallNotification;
    private ConfigurationPrefManager configurationPrefManager;
    private TextView mTextShare, mTextChangeUnit;
    private HashMap<String, Boolean> hashMap = new HashMap<>();
    private boolean isNavigationProgress = false;

    public static NavigationFilterScreen newInstance() {
        return new NavigationFilterScreen();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mPipModeBroadcastReceiver,
                new IntentFilter("app_in_pipmode"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_navigation, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("screenName", "Tripper Route Settings");
        REUtils.logGTMEvent(REConstants.ScreenViewManual, bundle);
        view.findViewById(R.id.cross_button).setOnClickListener(this);
        mTextShare = view.findViewById(R.id.textView_share);
        mTextShare.setOnClickListener(this);
        mTextChangeUnit = view.findViewById(R.id.textView_change_unit);
        mTextChangeUnit.setOnClickListener(this);
        //mSwitchTraffic = view.findViewById(R.id.livetraffic);
        mSwitchHighway = view.findViewById(R.id.highway);
        mSwitchToll = view.findViewById(R.id.tolls);
        mSwitchFerry = view.findViewById(R.id.ferry);
        mSwitchETADistance = view.findViewById(R.id.switch_eta_distance);
        mSwitchNightMode = view.findViewById(R.id.nightmode);
        mSwitchCallNotification = view.findViewById(R.id.switch_enable_call_notification);
        mSwitchCallNotification.setOnClickListener(this);
        //mSwitchTraffic.setOnCheckedChangeListener(this);
        mSwitchHighway.setOnCheckedChangeListener(this);
        mSwitchToll.setOnCheckedChangeListener(this);
        mSwitchFerry.setOnCheckedChangeListener(this);
        mSwitchETADistance.setOnCheckedChangeListener(this);
        mSwitchNightMode.setOnCheckedChangeListener(this);
        mSwitchCallNotification.setOnCheckedChangeListener(this);
        showHideCallNotificationSwitch();
        if (isNavigationProgress()) {
            mSwitchHighway.setVisibility(View.GONE);
            mSwitchToll.setVisibility(View.GONE);
            mSwitchFerry.setVisibility(View.GONE);
        }

        showConfigOptionsEnabled();
    }

    private void showHideCallNotificationSwitch() {
        if (REUtils.getFeatures().getDefaultFeatures().getTripperCallNotification().equalsIgnoreCase(REConstants.FEATURE_ENABLED)){
            mSwitchCallNotification.setVisibility(View.VISIBLE);
        } else {
            mSwitchCallNotification.setVisibility(View.GONE);
        }
    }

    private void showConfigOptionsEnabled() {
        configurationPrefManager = ConfigurationPrefManager.getInstance(getActivity());
        //mSwitchTraffic.setChecked(configurationPrefManager.isLiveTraffic());
        configurationPrefManager.setLiveTraffic(true);
        mSwitchHighway.setChecked(configurationPrefManager.isHighwaysEnabled());
        mSwitchToll.setChecked(configurationPrefManager.isTollRoad());
        mSwitchFerry.setChecked(configurationPrefManager.isFerriesEnabled());
        mSwitchETADistance.setChecked(configurationPrefManager.getETA());
        mSwitchNightMode.setChecked(configurationPrefManager.isNightModeEnabled());
        mSwitchCallNotification.setChecked(configurationPrefManager.getEnableCallNotification());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resize bottom sheet dialog so it doesn't span the entire width past a particular measurement
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels - 100;
        int height = -1; // MATCH_PARENT
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public void setRouteListener(ItemClickListener routeListener) {
        mListener = routeListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mPipModeBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.textView_share) {
            mListener.onNavigationShare();
            dismiss();
        } else if (view.getId() == R.id.textView_change_unit) {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Route Settings");
            params.putString("eventLabel", "Change Unit");
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
            Intent intent = new Intent(getActivity(), AppSettingActivity.class);
            startActivity(intent);
            dismiss();
        } else if (view.getId() == R.id.switch_enable_call_notification) {
            if (configurationPrefManager.getEnableCallNotification())
                REUtils.showErrorDialog(getActivity(),getActivity().getString(R.string.call_notification_below_twelve));
           // else
                //REUtils.showErrorDialog(getActivity(),getActivity().getString(R.string.ncall_notification_disabled));
        } else {
            mListener.onNavigationFilterClick();
            dismiss();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        /*if (buttonView.getId() == R.id.livetraffic) {
            configurationPrefManager.setLiveTraffic(isChecked);
        } else*/
        if (buttonView.getId() == R.id.highway) {
            configurationPrefManager.setHighways(isChecked);
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Avoid Highways");
            if (isChecked)
            {
                params.putString("eventLabel", "enable");
            }else {
                params.putString("eventLabel", "disable");
            }
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
        } else if (buttonView.getId() == R.id.tolls) {
            configurationPrefManager.setTollRoads(isChecked);
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Avoid Tolls");
            if (isChecked)
            {
                params.putString("eventLabel", "enable");
            }else {
                params.putString("eventLabel", "disable");
            }
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
        } else if (buttonView.getId() == R.id.ferry) {
            configurationPrefManager.setFerries(isChecked);
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Avoid Ferries");
            if (isChecked)
            {
                params.putString("eventLabel", "enable");
            }else {
                params.putString("eventLabel", "disable");
            }
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
        } else if (buttonView.getId() == R.id.nightmode) {
            configurationPrefManager.setNightmodeEnabled(isChecked);
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Night Mode");
            if (isChecked)
            {
                params.putString("eventLabel", "enable");
            }else {
                params.putString("eventLabel", "disable");
            }
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
        } else if (buttonView.getId() == R.id.switch_eta_distance) {
            configurationPrefManager.setETA(isChecked);
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "ETA");
            if (isChecked)
            {
                params.putString("eventLabel", "enable");
            }else {
                params.putString("eventLabel", "disable");
            }
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
        } else if (buttonView.getId() == R.id.switch_enable_call_notification) {
            configurationPrefManager.setEnableCallNotification(isChecked);
        }
    }

    public interface ItemClickListener {
        void onNavigationFilterClick();

        void onNavigationShare();
    }

    public boolean isNavigationProgress() {
        return isNavigationProgress;
    }

    public void setNavigationProgress(boolean navigationProgress) {
        isNavigationProgress = navigationProgress;
    }
    /**
     *
     */
    private BroadcastReceiver mPipModeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("app_in_pipmode".equals(intent.getAction()) && getActivity() != null) {
                // boolean b = intent.getBooleanExtra("connected", false);
                //String mDeviceStoredAddress = intent.getStringExtra("deviceaddress");
                dismiss();

            }
        }
    };
}
