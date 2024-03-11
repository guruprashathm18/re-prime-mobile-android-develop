package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.interactor

import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.listeners.OnFinishedListener

interface IFindMyMotorcycleInteractor {

    fun fetchDeviceData(deviceId: String, onFinishedListener: OnFinishedListener)

    fun fetchDataInEveryTenSecs(deviceId: String)
}