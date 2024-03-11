package com.royalenfield.bluetooth.ble;

import android.bluetooth.*;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GattCallback extends BluetoothGattCallback {


    private BleManager bleManager;
    public static BluetoothGatt mBluetoothGatt;

    public GattCallback(BleManager bleManager) {
        this.bleManager = bleManager;
    }


    private static final ParcelUuid RE_SERVICE_UUID = ParcelUuid
            .fromString("01FF0100-BA5E-F4EE-5CA1-EB1E5E4B1CE0");
    public static final UUID _DISPLAY_UNIT_UUID_ = UUID
            .fromString("01FF0100-BA5E-F4EE-5CA1-EB1E5E4B1CE0");
    public static final UUID DISPLAY_UNIT_UUID = UUID
            .fromString("01FF0101-BA5E-F4EE-5CA1-EB1E5E4B1CE0");
    private static final UUID CHARACTERISTIC_USER_DESCRIPTION_UUID = UUID
            .fromString("00002901-0000-1000-8000-00805f9b34fb");


    public static BluetoothGattCharacteristic characteristicToWrite;

    public static BluetoothGatt gattrequestMTU;

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                        int newState) {
        mBluetoothGatt = gatt;

        gattrequestMTU = gatt;

        Log.e("GattCallback", "onConnectionStateChange: status : " + status + " newState  " + newState);
        if (newState == BluetoothProfile.STATE_CONNECTED) {
//            connectionStateChanged.onChange(true);
//            bleManager.onConnectionChange(gatt,status,newState);
//            bleManager.showHideViews(true);
            bleManager.onConnectionChange(gatt, status, newState);
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//            gatt.close();
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            bleManager.showHideViews(false);
            bleManager.displayOtapProgress(false, 0, 0);
            bleManager.onConnectionChange(gatt, status, newState);
            //Status failure
//            bleManager.progressStatus(0);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//        try {
            Log.d("GattCallback", "onServicesDiscovered: status : " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> gattServices = gatt.getServices();
                String uuid = null;

                for (BluetoothGattService gattService : gattServices) {
                    UUID serviceUuid = gattService.getUuid();
                    if (serviceUuid.toString().equalsIgnoreCase(RE_SERVICE_UUID.toString())) {
                        List<BluetoothGattCharacteristic> gattCharacteristics =
                                gattService.getCharacteristics();
                        Log.e("onserviceDiscovered", "gattCharacteristics :" + gattCharacteristics.size());
                        for (BluetoothGattCharacteristic gattCharacteristic :
                                gattCharacteristics) {
                            Log.e("onserviceDiscovered", "gattCharacteristic :" + gattCharacteristic);
                            UUID charUuid = gattCharacteristic.getUuid();
                            Log.e("onserviceDiscovered", "charUuid :" + charUuid);
                            if (charUuid.toString().equalsIgnoreCase(DISPLAY_UNIT_UUID.toString())) {
                                mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
                                Log.e("onserviceDiscovered", "characteristicToWrite :" + characteristicToWrite);
                                characteristicToWrite = gattCharacteristic;
                                Log.e("onserviceDiscovered", "gattCharacteristicValue :" + Arrays.toString(gattCharacteristic.getValue()));
                                Log.e("onserviceDiscovered", "gattCharacteristicprop :" + gattCharacteristic.getProperties());
                                BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(CHARACTERISTIC_USER_DESCRIPTION_UUID);
                                Log.e("onserviceDiscovered", "descriptor :" + descriptor);
                                if (descriptor != null) {
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    boolean flag1 = mBluetoothGatt.writeDescriptor(descriptor);
                                    characteristicToWrite = gattCharacteristic;
                                    Log.e("onserviceDiscovered", "Subscribed");
                                }
                            } else {

                            }
                        }

                    }


                }


            }

            if (characteristicToWrite != null)
                bleManager.onCharacteristicFound(characteristicToWrite);
//        } catch (NullPointerException e) {
//            RELog.e(e);
//        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        byte[] reply = characteristic.getValue();
        Log.e("GattCallback", "onCharacteristicWriteRequest");
        bleManager.onCommandRecieved(reply);
//        bleManager.showHideViews(true);


        bleManager.processData(reply, bleManager);
//        new OtapProcessDisplayResponse().processReplyFromDisplay(reply);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        Log.e("GattCallback", "onCharacteristicWrite :" + Arrays.toString(characteristic.getValue()));
        bleManager.onCharacteristicWrite(gatt, characteristic, status);
        //sending time for clock sync if the device is already trusted
        byte[] bytearray = characteristic.getValue();
        if (bytearray != null && bytearray[0] == 33 && bytearray[1] == 0 && bytearray[18] == 64 && bytearray[19] == 69) {
            bleManager.sendTimeMsg();
        }
    }

}
