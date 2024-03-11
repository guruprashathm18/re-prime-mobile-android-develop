package com.royalenfield.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.royalenfield.bluetooth.ble.DeviceInfo;
import com.royalenfield.bluetooth.client.DeviceListContract;
import com.royalenfield.bluetooth.client.DeviceListFragment;
import com.royalenfield.bluetooth.otap.RELogger;
import com.royalenfield.bluetooth.otap.interactor.OtapInteractor;
import com.royalenfield.bluetooth.utils.BLEConstants;
import com.royalenfield.bluetooth.utils.BLEDeviceManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.home.rides.activity.ExitRideActivity;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.onboarding.AccountCreationSuccessActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This activity will displays the DevicesList by calling BLE Lib
 */
public class BleSearchActivity extends REBaseActivity implements DeviceListContract.View {

    DeviceListFragment mDeviceListFragment;
    boolean mIsBLEConnected;
    List<DeviceInfo> deviceInfoList = new ArrayList<>();
    OtapInteractor mOtapInteractor = new OtapInteractor();
    private Map<String, Object> mOtapMapData;
    private String mFirmwareId;
    private SharedPreferences.Editor mOtapPrefEditor;
    private static final int REQUESTCODE_BLE_LIB_STATUS = 5;
    private RELogger reLogger;

    /**
     * Receives the Status of OTAP from BLE Lib
     */

   /* private BroadcastReceiver mOtapStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("otapstatus".equals(intent.getAction())) {
                int status = intent.getIntExtra("status", -1);
                try {
                    if (REApplication.getInstance().getmConnectedDeviceInfo() != null && REApplication.getInstance().getmConnectedDeviceInfo().size() > 0) {
                        mOtapInteractor.saveFeedback(mFirmwareId, REApplication.getInstance().getmConnectedDeviceInfo().get(0).getAddress(), status);
                    }
                    if (status == 1) {
                        // Saving the latest updated file as current software version
                        if (mOtapMapData != null && mOtapMapData.get("firmwareVersion") != null) {
                            String deviceVersion = String.valueOf(mOtapMapData.get("firmwareVersion"));
                            if (!deviceVersion.isEmpty()) {
                                mOtapPrefEditor.putString("currentSoftwareVersion", deviceVersion);
                                mOtapPrefEditor.apply();
                                //Toast.makeText(context, "mOtapStatusReceiver:Storing deviceVersion:" + deviceVersion, Toast.LENGTH_SHORT).show();
                                reLogger.appendLog("mOtapStatusReceiver:Storing deviceVersion:" + deviceVersion);
                            }
                        }
                        runOnUiThread(() -> {
                            Intent intentsuccess = RESuccessSplashActivity.getIntent(getApplicationContext(), null,
                                    getResources().getString(R.string.software_update_success));
                            startActivity(intentsuccess);
                            overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                        });
                    } else if (status == 0) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Software Update Failed", Toast.LENGTH_SHORT).show());
                    } else if (status == 2) {
//                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Software Update Failed", Toast.LENGTH_SHORT).show());
                    }

                } catch (Exception e) {
                    RELog.e(e);
                }
            }
        }
    };*/

    /**
     * Receives the Status of OTAP from BLE Lib
     */
    private BroadcastReceiver mDevicePairedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("devicePaired".equals(intent.getAction())) {
                Intent mIntent = new Intent(getApplicationContext(), AccountCreationSuccessActivity.class);
                mIntent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.BLE_DEVICE_PAIRED);
                startActivity(mIntent);
                overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            }
        }
    };

    /**
     * Receives the Status of OTAP from BLE Lib
     */
    private BroadcastReceiver mUserConfirmationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("deviceUserConfirmation".equals(intent.getAction())) {
                Intent mIntent = new Intent(getApplicationContext(), ExitRideActivity.class);
                mIntent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.BLE_ACTION_CONFIRMATION);
                mIntent.putExtra("type", intent.getStringExtra("type"));
                mIntent.putExtra(BLEConstants.BLE_DEVICE_POSITION,
                        intent.getIntExtra(BLEConstants.BLE_DEVICE_POSITION, -1));
                startActivity(mIntent);
                overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            }
        }
    };


    private BroadcastReceiver mBLEDialogReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BLEConstants.BLE_INTENT_SHOW_DIALOG.equals(intent.getAction())) {
                REUtils.showErrorDialog(BleSearchActivity.this, "Please unpair an existing device to connect a new one");
            }
        }
    };

    private BroadcastReceiver mBLEBackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BLEConstants.BLE_BACK_PRESS.equals(intent.getAction())) {
                closeActivity();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString("screenName", "Tripper Navigation Devices");
        REUtils.logGTMEvent(REConstants.ScreenViewManual, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reLogger=new RELogger(this);
        REApplication.getInstance().setLoggerFileCreated(false);
        reLogger.appendLog("BleSearchActivity --- Oncreate");
        getIntentData();
        setContentView(R.layout.activity_ble_search);
        SharedPreferences mOtapPreference = getApplicationContext().getSharedPreferences("RE_APP", Context.MODE_PRIVATE);
        mOtapPrefEditor = mOtapPreference.edit();
        FragmentFrameHolder mBLEFrame = findViewById(R.id.ble_search_fragment);
        mDeviceListFragment = new DeviceListFragment();
        deviceInfoList = BLEDeviceManager.getMyTBTList(getApplicationContext());
        List<DeviceInfo> connectedDeviceList = REApplication.getInstance().getmConnectedDeviceInfo();
        mIsBLEConnected = connectedDeviceList != null && connectedDeviceList.size() > 0;
        if (!mIsBLEConnected) {
            BLEDeviceManager.setDeviceConnectionsToFalse(deviceInfoList, getApplicationContext());
        }
        //multi device whitelist fix
        if (deviceInfoList!=null && deviceInfoList.size()>0) {
            for (int i = 0; i < deviceInfoList.size(); i++) {
                deviceInfoList.get(i).setConnected(false);
            }
        }
        if (connectedDeviceList!=null && connectedDeviceList.size()>0){
            for (int i = 0; i < deviceInfoList.size(); i++) {
                if (deviceInfoList.get(i).getAddress().equalsIgnoreCase(connectedDeviceList.get(0).getAddress())){
                    deviceInfoList.get(i).setConnected(REApplication.getInstance().ismIsDeviceConnected());
                }
            }
        }
        //Update TBT list after updating connection status for multi device whitelisting fix
        BLEDeviceManager.updateMyTBTDevice(deviceInfoList, BleSearchActivity.this);

        if (mOtapMapData != null) {
            mDeviceListFragment.setListener(this, mIsBLEConnected, mOtapMapData);
            Log.e("mOtapMapDataaa",""+mOtapMapData.get("firmwareUrl"));
        } else {
            mOtapMapData = new HashMap<>();
            mOtapMapData.put("description", "");
            mOtapMapData.put("firmwareUrl", "");
            mOtapMapData.put("firmwareMappingUrl", "");
            mOtapMapData.put("firmwareVersion", "");
            mOtapMapData.put("releaseDate", "");
            mOtapMapData.put("updateType", "");
            mDeviceListFragment.setListener(this, mIsBLEConnected, mOtapMapData);
        }
        mBLEFrame.loadFragment(this, mDeviceListFragment, "");
       /* LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mOtapStatusReceiver,
                new IntentFilter("otapstatus"));*/
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mDevicePairedReceiver,
                new IntentFilter("devicePaired"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mUserConfirmationReceiver,
                new IntentFilter("deviceUserConfirmation"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBLEDialogReceiver,
                new IntentFilter(BLEConstants.BLE_INTENT_SHOW_DIALOG));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBLEBackReceiver,
                new IntentFilter(BLEConstants.BLE_BACK_PRESS));
    }

    @Override
    protected void onDestroy() {
        //LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mOtapStatusReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mDevicePairedReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mUserConfirmationReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBLEDialogReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBLEBackReceiver);

        super.onDestroy();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            mOtapMapData = (Map<String, Object>) getIntent().getSerializableExtra("otapmap");
            if (mOtapMapData != null && mOtapMapData.get("firmwareVersion") != null) {
                mFirmwareId = mOtapMapData.get("firmwareVersion").toString();
                reLogger.appendLog("BleSearchAvtivity_mFirmwareId "+mFirmwareId);
            }
        }
    }

    @Override
    public void showScanningProgress() {
        showLoading();
    }

    @Override
    public void updateDevice(@NotNull DeviceInfo result) {
    }

    @Override
    public void dismissProgress() {
        hideLoading();
    }


    private void updateConnectionState(boolean isConnected) {
        if (!isConnected) {
            REApplication.getInstance().setmConnectedDeviceInfo(new ArrayList<>());
        }
    }

    @Override
    public void showError(@NotNull String error) {
    }


    @Override
    public void connectedDeviceInfo(@NotNull List<DeviceInfo> list) {
        REApplication.getInstance().setmConnectedDeviceInfo(list);
        if (list.size() == 0) {
            updateConnectionState(false);
        } else {
            updateConnectionState(true);
        }
    }

    @Override
    public void onScanStart() {
    }

    @Override
    public void onScanStop() {
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    private void closeActivity() {
        Intent intent = new Intent();
        setResult(REQUESTCODE_BLE_LIB_STATUS, intent);
        finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }


    @Override
    public void showHideInstallView(boolean b) {

    }

    @Override
    public void onScanInitFailed() {

    }
}