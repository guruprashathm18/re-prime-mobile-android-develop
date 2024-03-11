package com.royalenfield.bluetooth.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattServer;

import java.util.List;

public class BLEModel {

    private static BLEModel modelStore;

    private BluetoothGatt bluetoothGatt;

    private BluetoothGattServer bluetoothGattServer;

    private List<DeviceInfo> connectedDeviceInfo;

    private boolean isScanning =false;

    private boolean isManualDisconnect = false;

    public boolean isManualDisconnect() {
        return isManualDisconnect;
    }

    public void setManualDisconnect(boolean manualDisconnect) {
        isManualDisconnect = manualDisconnect;
    }

    public boolean isScanning() {
        return isScanning;
    }

    public void setScanning(boolean scanning) {
        isScanning = scanning;
    }

    public boolean isDeviceConnected() {
        return isDeviceConnected;
    }

    public void setDeviceConnected(boolean deviceConnected) {
        isDeviceConnected = deviceConnected;
    }

    private boolean isDeviceConnected;

    public List<DeviceInfo> getConnectedDeviceInfo() {
        return connectedDeviceInfo;
    }

    public void setConnectedDeviceInfo(List<DeviceInfo> connectedDeviceInfo) {
        this.connectedDeviceInfo = connectedDeviceInfo;
    }

    public BluetoothGattServer getBluetoothGattServer() {
        return bluetoothGattServer;
    }

    public void setBluetoothGattServer(BluetoothGattServer bluetoothGattServer) {
        this.bluetoothGattServer = bluetoothGattServer;
    }

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public static BLEModel getInstance() {
        if (modelStore == null) {
            modelStore = new BLEModel();
        }
        return modelStore;
    }
}
