package com.royalenfield.reprime.ui.home.connected.motorcycle.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CommonResponseModel<T> {
    @SerializedName("error")
    @Expose
    var error: Boolean = false

    @SerializedName("code")
    @Expose
    var code: String = ""

    @SerializedName("errorMessage")
    @Expose
    var errorMessage: Any? = null

    @SerializedName("data")
    @Expose
    var data: T? = null
}