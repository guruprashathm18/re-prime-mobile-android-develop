package com.royalenfield.bluetooth.ble

data class  DeviceInfo(var mobileDeviceOS: String ?,var status: String ?, var isTrusted: Boolean,var isConnected: Boolean
                       ,var name: String?, var address: String?, var timeStampNano: Long,var deviceToken: String, var serialNumber: String, var userGUID: String, var OSVersion: String) {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DeviceInfo) {
            return false
        } else if (address?.hashCode() == other.address?.hashCode()) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        return address.hashCode()
    }
}