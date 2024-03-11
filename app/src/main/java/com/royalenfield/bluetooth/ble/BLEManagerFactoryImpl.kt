package com.royalenfield.bluetooth.ble


class BLEManagerFactoryImpl : IBLEManagerFactory {
    override fun getBleManager(): IBleManager {
        return BleManager()
    }
}