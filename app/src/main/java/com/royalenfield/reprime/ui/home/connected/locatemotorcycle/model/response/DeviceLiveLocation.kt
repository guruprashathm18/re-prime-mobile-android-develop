package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeviceLiveLocation {

    @SerializedName("timestamp")
    @Expose
    private val timestamp: String? = null

    @SerializedName("latitude")
    @Expose
    private val latitude: Double? = null

    @SerializedName("longitude")
    @Expose
    private val longitude: Double? = null

    @SerializedName("gpsStatus")
    @Expose
    private val gpsStatus: String? = null

    @SerializedName("satellites")
    @Expose
    private val satellites: Any? = null

    @SerializedName("idlingStatus")
    @Expose
    private val idlingStatus: Boolean? = null

    @SerializedName("haltStatus")
    @Expose
    private val haltStatus: Boolean? = null

    @SerializedName("isOverspeed")
    @Expose
    private val isOverspeed: Boolean? = null

    @SerializedName("isHA")
    @Expose
    private val isHA: Boolean? = null

    @SerializedName("isHB")
    @Expose
    private val isHB: Boolean? = null

    @SerializedName("isPrimaryBattery")
    @Expose
    private val isPrimaryBattery: Boolean? = null

    @SerializedName("isNoGps")
    @Expose
    private val isNoGps: Boolean? = null

    @SerializedName("speed")
    @Expose
    private val speed: Int? = null

    @SerializedName("extBatVol")
    @Expose
    private val extBatVol: Int? = null

    @SerializedName("plusCode")
    @Expose
    private val plusCode: String? = null

    @SerializedName("address")
    @Expose
    private val address: String? = null

    @SerializedName("event_flag")
    @Expose
    private val eventFlag: String? = null

}
