package com.royalenfield.reprime.ui.home.connected.motorcycle.interactor

import com.royalenfield.reprime.ui.home.connected.motorcycle.listener.MotorcycleListener

interface IMotorcycleInteractor {

    fun fetchOBDDataPoints(deviceId: String, listener: MotorcycleListener)

    fun fetchDataInEveryTenSecs(deviceId: String)
}