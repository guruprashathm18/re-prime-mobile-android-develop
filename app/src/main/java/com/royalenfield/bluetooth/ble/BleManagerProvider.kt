package com.royalenfield.bluetooth.ble

object BleManagerProvider {
    fun getInstance(): IBleManager {
        return BleManager()
    }


}