package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeviceLatestLocation {

    @SerializedName("vehicleNumber")
    @Expose
    var vehicleNumber: String = ""

    @SerializedName("vehicleType")
    @Expose
    var vehicleType: String = ""

    @SerializedName("vehicleModel")
    @Expose
    var vehicleModel: String = ""

    @SerializedName("vehicleGroups")
    @Expose
    var vehicleGroups: List<Any>? = null

    @SerializedName("timestamp")
    @Expose
    var timestamp: String = ""

    @SerializedName("lastIgnitionTime")
    @Expose
    var lastIgnitionTime: String = ""

    @SerializedName("latitude")
    @Expose
    var latitude: Double = 0.0

    @SerializedName("longitude")
    @Expose
    var longitude: Double = 0.0

    @SerializedName("gpsStatus")
    @Expose
    var gpsStatus: String = ""

    @SerializedName("isOffline")
    @Expose
    var isOffline: Boolean = false

    @SerializedName("address")
    @Expose
    var address: String = ""

    @SerializedName("uniqueId")
    @Expose
    var uniqueId: String = ""

    @SerializedName("satellites")
    @Expose
    var satellites: Any? = null

    @SerializedName("idlingStatus")
    @Expose
    var idlingStatus: Boolean = false

    @SerializedName("haltStatus")
    @Expose
    var haltStatus: Boolean = false

    @SerializedName("isOverspeed")
    @Expose
    var isOverspeed: Boolean = false

    @SerializedName("isHA")
    @Expose
    var isHA: Boolean = false

    @SerializedName("isHB")
    @Expose
    var isHB: Boolean = false

    @SerializedName("isPrimaryBattery")
    @Expose
    var isPrimaryBattery: Boolean = false

    @SerializedName("isNoGps")
    @Expose
    var isNoGps: Boolean = false

    @SerializedName("speed")
    @Expose
    var speed: Int = 0

    @SerializedName("extBatVol")
    @Expose
    var extBatVol: Int = 0

    @SerializedName("plusCode")
    @Expose
    var plusCode: String = ""
    @SerializedName("isNoData")
    @Expose
     var isNoData: Boolean? = null

    @SerializedName("eventFlag")
    @Expose
     var eventFlag: Any? = null

}
