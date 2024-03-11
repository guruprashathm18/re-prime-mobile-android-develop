package com.royalenfield.reprime.ui.home.connected.motorcycle.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class SettingResponseModel : Serializable {

    @SerializedName("locationAccessFlag")
    @Expose
    var locationAccessFlag: Boolean = false

    @SerializedName("notificationFlag")
    @Expose
    var notificationFlag: Boolean = false

    @SerializedName("guid")
    @Expose
    var guid: String = ""

    @SerializedName("deviceSerialNumber")
    @Expose
    var deviceSerialNumber: String = ""

    @SerializedName("imei")
    @Expose
    var imei: String = ""

    @SerializedName("firmwareVersion")
    @Expose
    var firmwareVersion: String = ""

    @SerializedName("vin")
    @Expose
    var vin: String = ""


    @SerializedName("configVersion")
    @Expose
    var configVersion: Double = 0.0


}