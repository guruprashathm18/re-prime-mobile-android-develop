package com.royalenfield.reprime.ui.home.connected.motorcycle.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SettingRequestModel(@SerializedName("guid")
                          @Expose var guid: String, @SerializedName("imeiNumber")
                          @Expose var deviceSerialNumber: String, @SerializedName("locationAccessFlag")
                          @Expose var locationAccessFlag: Boolean, @SerializedName("notificationFlag")
                          @Expose var notificationFlag: Boolean)

class SettingRequestModelRead(@SerializedName("guid")
                          @Expose var guid: String, @SerializedName("imeiNumber")
                          @Expose var deviceSerialNumber: String)
