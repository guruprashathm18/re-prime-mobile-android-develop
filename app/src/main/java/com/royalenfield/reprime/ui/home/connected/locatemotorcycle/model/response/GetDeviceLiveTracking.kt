package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetDeviceLiveTracking {

    @SerializedName("deviceLiveTracking")
    @Expose
    val deviceLiveLocation: DeviceLiveLocation? = null

}