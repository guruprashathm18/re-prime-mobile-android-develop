package com.royalenfield.bluetooth.client

import com.royalenfield.bluetooth.ble.DeviceInfo


interface DeviceListContract {

    interface View {
        fun showScanningProgress()
        fun updateDevice(result: DeviceInfo)
        fun dismissProgress()
        fun showError(error: String)
        fun connectedDeviceInfo(deviceInfo: MutableList<DeviceInfo>)
        fun onScanStop()
        fun onScanStart()
        fun onScanInitFailed()

        //OTAP
        fun showHideInstallView(boolean: Boolean)
    }

    interface OnDisconnectClickListener {
        fun onDisconnectClicked(forget: Boolean, position: Int)
    }

}