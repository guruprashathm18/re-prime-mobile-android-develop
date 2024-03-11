package com.royalenfield.bluetooth.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.royalenfield.bluetooth.ble.DeviceInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.royalenfield.reprime.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BLEDeviceManager {

    /**
     * Returns the saved device list by fetching from preference
     * @param context : Context
     * @return : List<DeviceInfo></DeviceInfo>
     */
    public static List<DeviceInfo> getMyTBTList(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().
                    getString(R.string.preference_file_key), MODE_PRIVATE);
            List<DeviceInfo> deviceInfoList = null;
            try {
                deviceInfoList = new Gson().fromJson(sharedPreferences.getString(BLEConstants.BLE_PREF_MY_TBT_LIST_KEY,
                        null),
                        new TypeToken<List<DeviceInfo>>() {
                        }.getType());
            } catch (Exception e){
                updateMyTBTDevice(new ArrayList<>(),context);
            }
            if (deviceInfoList != null) {
                return deviceInfoList;
            } else {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }


    // For getting the list of iOS connected Tripper Devices
    public static List<DeviceInfo> getMyiOSTBTList(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().
                    getString(R.string.preference_file_key_iosconnected_trippers), MODE_PRIVATE);
            List<DeviceInfo> deviceInfoList = new Gson().fromJson(sharedPreferences.getString(BLEConstants.BLE_PREF_MY_IOS_TBT_LIST_KEY,
                    null),
                    new TypeToken<List<DeviceInfo>>() {
                    }.getType());
            if (deviceInfoList != null) {
                return deviceInfoList;
            } else {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
    /**
     * This method is used to iterate through all the saved devices & return whether the device is trusted or not
     * @param address : String
     * @return : boolean
     */
    public static boolean isTrustedDevice(String address, Context context) {
        List<DeviceInfo> deviceInfoList = getMyTBTList(context);
        if (deviceInfoList.size() > 0) {
            for (int i = 0; i<deviceInfoList.size(); i++) {
                if (address.equals(deviceInfoList.get(i).getAddress())) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static void updateMyTBTDevice(List<DeviceInfo> deviceInfoList, Context context) {
       SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().
               getString(R.string.preference_file_key), MODE_PRIVATE).edit();
       editor.putString(BLEConstants.BLE_PREF_MY_TBT_LIST_KEY, new Gson().toJson(deviceInfoList));
       editor.apply();
    }
    // update iOS connected Tripper devices
    public static void updateMyiOS_Connected_TBTDevice(List<DeviceInfo> deviceInfoList, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().
                getString(R.string.preference_file_key_iosconnected_trippers), MODE_PRIVATE).edit();
        editor.putString(BLEConstants.BLE_PREF_MY_IOS_TBT_LIST_KEY, new Gson().toJson(deviceInfoList));
        editor.apply();
    }

    public static void saveDeviceToMyTBTList(DeviceInfo deviceInfo, Context context) {
        List<DeviceInfo> deviceInfoList = getMyTBTList(context);
        deviceInfoList.add(deviceInfo);
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().
                getString(R.string.preference_file_key), MODE_PRIVATE).edit();
        editor.putString(BLEConstants.BLE_PREF_MY_TBT_LIST_KEY, new Gson().toJson(deviceInfoList));
        editor.apply();
    }

    public static void setAutoConnectUserPreference(Context context, boolean isAutoConnect) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().
                getString(R.string.preference_file_key), MODE_PRIVATE).edit();
        editor.putBoolean(BLEConstants.BLE_AUTO_CONNECT_PREF, isAutoConnect);
        editor.apply();
    }

    public static boolean isAutoConnectEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().
                getString(R.string.preference_file_key), MODE_PRIVATE);
        return sharedPreferences.getBoolean(BLEConstants.BLE_AUTO_CONNECT_PREF, true);
    }

    public static void setDeviceConnectionsToFalse(List<DeviceInfo> list, Context context) {
        for (int i = 0; i<list.size(); i++) {
            list.get(i).setConnected(false);
        }
        updateMyTBTDevice(list, context);
    }

    public static void updateConnectedDeviceStatus(Context context, String address, List<DeviceInfo> mStoredDevicesList) {
        if (mStoredDevicesList != null && mStoredDevicesList.size() > 0) {
            updateDeviceStatus(mStoredDevicesList, address, true);
            updateMyTBTDevice(mStoredDevicesList, context);
        }
    }

    private static void updateDeviceStatus(List<DeviceInfo> mStoredDevicesList, String address, boolean b) {
        for (int i = 0; i < mStoredDevicesList.size(); i++) {
            if (address.equals(mStoredDevicesList.get(i).getAddress())) {
                mStoredDevicesList.get(i).setConnected(b);
            }
        }
    }

    public static void updateDisconnectedDeviceStatus(Context context, String address, List<DeviceInfo> mStoredDevicesList) {
        if (mStoredDevicesList != null && mStoredDevicesList.size() > 0) {
            updateDeviceStatus(mStoredDevicesList, address, false);
            updateMyTBTDevice(mStoredDevicesList, context);
        }
    }
}
