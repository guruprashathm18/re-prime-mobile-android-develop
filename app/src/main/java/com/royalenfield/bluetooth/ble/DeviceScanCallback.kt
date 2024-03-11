package com.royalenfield.bluetooth.ble

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings.CALLBACK_TYPE_ALL_MATCHES
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.royalenfield.bluetooth.ble.BleManagerProvider.getInstance
import com.royalenfield.bluetooth.utils.BLEConstants
import com.royalenfield.reprime.application.REApplication


class DeviceScanCallback() : ScanCallback() {

    private val manager = getInstance()

    override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)

        if (callbackType == CALLBACK_TYPE_ALL_MATCHES) {
            with(result) {
                val scanresults = Intent(BLEConstants.BLE_INTENT_SCAN_RESUlTS)
                scanresults.putExtra(BLEConstants.BLE_DEVICE_NAME, device?.name)
                scanresults.putExtra(BLEConstants.BLE_DEVICE_ADDRESS, device.address)
                LocalBroadcastManager.getInstance(REApplication.getAppContext())
                    .sendBroadcast(scanresults)
            }
        }


    }


    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        val scanresults = Intent(BLEConstants.BLE_SCAN_FAILED)
        LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(scanresults)
    }
}

