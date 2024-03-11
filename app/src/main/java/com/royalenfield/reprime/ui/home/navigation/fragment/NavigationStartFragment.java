package com.royalenfield.reprime.ui.home.navigation.fragment;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.royalenfield.bluetooth.ble.DeviceInfo;
import com.royalenfield.bluetooth.BleSearchActivity;
import com.royalenfield.bluetooth.otap.listener.OnOTAPCallback;
import com.royalenfield.bluetooth.utils.BLEDeviceManager;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.fragments.RENavigationRootFragment;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static android.app.Activity.RESULT_OK;

/**
 * This fragment is opened
 */
public class NavigationStartFragment extends REBaseFragment implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, OnOTAPCallback {

    private String mNavType;
    private ConstraintLayout mStopNavigation,mConstraintNormal, mConstraintPipmode ;
    private ToggleButton btnRecordPause;
    private Button btnRecordStop;
    private ImageView mHelmetIcon,mHelmetIconPip;
    private TextView mHelperText,mPipInfoText,mPipDeviceDiconnectedInfo;
    private Map<String, Object> mOtapMapData;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSION_SETTING = 2;
    private FragmentActivity mContext;
    private static NavigationStartFragment instance = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getBundleData();
        View view = inflater.inflate(R.layout.fragment_navigation_grayed, container, false);
        instance = this;
        mHelmetIcon = view.findViewById(R.id.imageView_helmet);
        mHelmetIcon.setOnClickListener(this);
        mHelmetIconPip = view.findViewById(R.id.imageView_helmet_pip);
        mHelperText = view.findViewById(R.id.tv_nav_msg);
        mPipInfoText = view.findViewById(R.id.tv_nav_msg_pip);
        mPipDeviceDiconnectedInfo = view.findViewById(R.id.tv_nav_msg_pip_ble_disconnected_info);
        mStopNavigation = view.findViewById(R.id.stop_navigation);
        mConstraintPipmode = view.findViewById(R.id.constraint_pipScreen);
        mConstraintNormal = view.findViewById(R.id.constraint_start);
        btnRecordPause = view.findViewById(R.id.record_pause);
        btnRecordPause.setOnCheckedChangeListener(this);
        btnRecordStop = view.findViewById(R.id.record_stop);
        btnRecordStop.setOnClickListener(this);
        FirestoreManager.getInstance().getOTAPInfo(this);
        manageViews();
        registerListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //check whether the app was in pip mode and reached destination("Arrived at Destination")
        if (mPipInfoText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.nav_msg_arrived))) {
            mStopNavigation.performClick();
           //Change back the text to Navigation message
            mPipInfoText.setText(getResources().getString(R.string.nav_msg));
        }
    }

    public static NavigationStartFragment getInstance() {
        return instance;
    }

    private void registerListeners() {
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mconnectionBroadcastReceiver,
                    new IntentFilter("updateconnection"));
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mAuthBroadcastReceiver,
                    new IntentFilter("pinAuth"));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mconnectionBroadcastReceiver);
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mAuthBroadcastReceiver);
            try {
                REPreference.getInstance().putBoolean(getActivity(),"pip",false);
            } catch (PreferenceException e) {
                e.printStackTrace();
            }
        }
    }

    private void manageViews() {
        if (mNavType != null) {
            if (mNavType.equals(REConstants.NAVIGATION_TYPE_STOP)) {
                mStopNavigation.setVisibility(View.VISIBLE);
                mStopNavigation.setOnClickListener(this);
            } else if (mNavType.equals(REConstants.NAVIGATION_TYPE_RECORD)) {
                mStopNavigation.setVisibility(View.GONE);
                btnRecordPause.setVisibility(View.VISIBLE);
                btnRecordStop.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getBundleData() {
        if (getArguments() != null) {
            mNavType = getArguments().getString(REConstants.NAVIGATION_TYPE_KEY);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.stop_navigation) {
            updateNavigationFragment(mNavType);
            removeFragment();
        } else if (view.getId() == R.id.record_stop) {
            updateNavigationFragment(REConstants.NAVIGATION_TYPE_RECORD_STOP);
            removeFragment();
        } else if (view.getId() == R.id.imageView_helmet) {
            if (getActivity() != null) {
                if (hasBackgroundLocationPermission()) {
                    checkBluetooth();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        try {
                            REPreference.getInstance().putString(getActivity(), "Nav_Location", "loc_disabled");
                        } catch (PreferenceException e) {
                            RELog.e(e);
                        }
                    }
                    displayNeverAskAgainDialog();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                openBleSearchActivity();
            } else {
                // User declined to enable Bluetooth, exit the app.
                Toast.makeText(getActivity(), "Bluetooth Not Enabled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PERMISSION_SETTING && getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                displayNeverAskAgainDialog();
            }
        }
    }

    private boolean hasBackgroundLocationPermission() {
        boolean hasBackgroundPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                hasBackgroundPermission = true;
            }
        } else {
            hasBackgroundPermission = true;
        }
        return hasBackgroundPermission;
    }

    private void openBleSearchActivity() {
        if (REUtils.isLocationEnabled(getContext())) {
            Intent intent = new Intent(getActivity(), BleSearchActivity.class);
            if (mOtapMapData != null) {
                intent.putExtra("otapmap", (Serializable) mOtapMapData);
            }
            startActivity(intent);
            mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        } else {
            Toast.makeText(getActivity(), "Please enable location and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFragment() {
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    // when app is in pip mode returns true, and false if comes back to normal mode.
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        handleUiforPipMode(isInPictureInPictureMode);
    }

    // Control the ui in pip mode and navigates back to app from pip
    private void handleUiforPipMode(boolean value) {
        mConstraintNormal.setVisibility(value ? View.GONE:View.VISIBLE);
        mConstraintPipmode.setVisibility(value ? View.VISIBLE:View.GONE);
        RENavigationRootFragment.getInstance().showorHideTitle(value);
        Activity activity = getActivity();
        if(activity instanceof REHomeActivity){
            REHomeActivity myactivity = (REHomeActivity) activity;
            myactivity.mShowOrHideActionbaritems(value);
        }
        try {
            // Boolean reference to add the app is in pip mode or not
            REPreference.getInstance().putBoolean(getActivity(),"pip",value);
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            btnRecordPause.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.pause), null, null, null);
            RELog.e("test","Resume called");
            updateNavigationFragment(REConstants.NAVIGATION_TYPE_RECORD_RESUME);

        } else {
            btnRecordPause.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.play_test), null, null, null);
            RELog.e("test","pause called");
            updateNavigationFragment(REConstants.NAVIGATION_TYPE_RECORD_PAUSE);
        }
    }

    private void updateNavigationFragment(String navigationTypeRecordResume) {
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    new Intent().putExtra(REConstants.NAVIGATION_TYPE_KEY, navigationTypeRecordResume)
            );
        }
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getResources().getString(R.string.customalert_title));
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion >= Build.VERSION_CODES.Q) {
            builder.setMessage(getResources().getString(R.string.text_bg_location_android_10));
        } else {
            builder.setMessage(getResources().getString(R.string.text_bg_location_android_9));
        }
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.customalert_ok), (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(getResources().getString(R.string.text_package),
                    mContext.getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
        });
        //Abuilder.setNegativeButton("Cancel", null);
        builder.show();
    }

    /**
     * Checks and allows to enable bluetooth if disabled.
     */
    private void checkBluetooth() {
        if (getActivity() != null) {
            BluetoothAdapter mBluetoothAdapter = ((BluetoothManager)
                    Objects.requireNonNull(getActivity().getSystemService(Context.BLUETOOTH_SERVICE)))
                    .getAdapter();
            // Is Bluetooth supported on this device?
            if (mBluetoothAdapter != null) {
                // Is Bluetooth turned on?
                if (!mBluetoothAdapter.isEnabled()) {
                    // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    openBleSearchActivity();
                }
            } else {
                // Bluetooth is not supported.
                Toast.makeText(getActivity(), "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Sets the helmet icon based on connection status
     *
     * @param isConnected
     */
    private void manageConnectionStatus(boolean isConnected) {
        if (!isConnected) {
            mHelmetIcon.setImageDrawable(getResources().
                    getDrawable(R.drawable.red_helmet_ic));
            mHelmetIconPip.setImageDrawable(getResources().
                    getDrawable(R.drawable.red_helmet_ic));
            mHelperText.setText(getResources().getString(R.string.nav_msg_disconnected));
           // mPipInfoText.setText(getResources().getString(R.string.nav_msg_disconnected));
            mPipDeviceDiconnectedInfo.setVisibility(View.VISIBLE);
            mPipInfoText.setVisibility(View.GONE);
        } else {
            mHelmetIcon.setImageDrawable(getResources().
                    getDrawable(R.drawable.green_helmet_ic));
            mHelmetIconPip.setImageDrawable(getResources().
                    getDrawable(R.drawable.green_helmet_ic));
            mHelperText.setText(getResources().getString(R.string.nav_msg));
          //  mPipInfoText.setText(getResources().getString(R.string.nav_msg));
            mPipDeviceDiconnectedInfo.setVisibility(View.GONE);
            mPipInfoText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOTAPFirestoreSuccess(Map<String, Object> map) {
        mOtapMapData = map;
    }

    @Override
    public void onOTAPFirestoreFailure(String message) {

    }

    /**
     *
     */
    private BroadcastReceiver mconnectionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("updateconnection".equals(intent.getAction())) {
                boolean isConnected = intent.getBooleanExtra("connected", false);
                String mDeviceStoredAddress = intent.getStringExtra("deviceaddress");
                if (!isConnected) {
                    manageConnectionStatus(false);
                } else {
                    List<DeviceInfo> deviceInfoList = BLEDeviceManager.getMyTBTList(getContext());
                    if (deviceInfoList.size() > 0) {
                        for (int i = 0; i < deviceInfoList.size(); i++) {
                            if (deviceInfoList.get(i).getAddress().equals(mDeviceStoredAddress)) {
                                manageConnectionStatus(isConnected); // Updating only if it is authorised & saved
                            }
                        }
                    } else {
                        manageConnectionStatus(false);
                    }
                }
            }
        }
    };

    /**
     *
     */
    private BroadcastReceiver mAuthBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("pinAuth".equals(intent.getAction())) {
                boolean isConnected = intent.getBooleanExtra("auth", false);
                manageConnectionStatus(isConnected);
            }
        }
    };
// change the text in pip screen
    public void changePipInfoText(){
        //Change the pip info text to "Arrived at Destination" when destination is reached
         mContext.runOnUiThread(() -> mPipInfoText.setText(getResources().getString(R.string.nav_msg_arrived)));
    }
}
