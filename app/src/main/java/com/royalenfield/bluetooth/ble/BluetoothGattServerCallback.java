package com.royalenfield.bluetooth.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

public class BluetoothGattServerCallback extends android.bluetooth.BluetoothGattServerCallback {

    private BleManager bleManager;

    public BluetoothGattServerCallback(BleManager bleManager) {
        this.bleManager = bleManager;
    }


    @Override
    public void onConnectionStateChange(final BluetoothDevice device, int status, int newState) {
        super.onConnectionStateChange(device, status, newState);
        Log.e("GattServerCallback", "onConnectionStateChange status : " + status + " newState  " + newState);
    }

    @Override
    public void onServiceAdded(int status, BluetoothGattService service) {
        super.onServiceAdded(status, service);
        Log.e("GattServerCallback", "onServiceAdded");
//        bleManager.connectDevice();

    }

    @Override
    public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
        Log.e("GattServerCallback", "onCharacteristicReadRequest");

    }

    @Override
    public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic,
                                             boolean preparedWrite, boolean responseNeeded, int offset, final byte[] value) {
        super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
        Log.e("GattServerCallback", "onCharacteristicWriteRequest");
        bleManager.onCommandRecieved(value);
        bleManager.processData(value, bleManager);
    }
}
