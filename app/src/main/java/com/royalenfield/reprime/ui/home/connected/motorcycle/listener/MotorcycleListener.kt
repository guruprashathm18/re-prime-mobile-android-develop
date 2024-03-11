package com.royalenfield.reprime.ui.home.connected.motorcycle.listener

import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.OBDResponseData
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleResponseModel
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingResponseModel

interface MotorcycleListener {

    fun onSuccess(payload: OBDResponseData)

    fun onFailure(errorMessage: String)

    fun onPairingSuccess(deviceData: PairingMotorcycleResponseModel.GetDeviceData)

    fun onPairingFailure(errorMessage: String?)

    fun onReadSettingsSuccess(settings: SettingResponseModel)

    fun onUpdateSettingsSuccess(msg: String)

    fun onReadSettingsFailure(errorMessage: String?)

    fun onUpdateSettingsFailure(errorMessage: String?)

    fun onProvisionUpdateStatusSuccess(msg: String)

    fun onProvisionUpdateStatusFailure(errorMessage: String)


}