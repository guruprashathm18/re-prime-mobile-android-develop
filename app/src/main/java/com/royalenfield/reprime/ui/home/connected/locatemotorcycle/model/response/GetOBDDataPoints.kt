package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class GetOBDDataPoints {
    @SerializedName("getObdDataPoints")
    @Expose
    val obdDataPoints: List<OBDDataPoints>? = null

     @SerializedName("healthAlertsCount")
     @Expose
     val healthAlertsCount: Int = 0

     @SerializedName("lowBattery")
     @Expose
     val lowBattery: Boolean = false

     @SerializedName("lowFuel")
     @Expose
     val lowFuel: Boolean = false


     @SerializedName("haltStatus")
     @Expose
     val haltStatus: Boolean? = null

 }