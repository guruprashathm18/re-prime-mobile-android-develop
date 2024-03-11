package com.royalenfield.reprime.models.response.web.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.response.web.login.LoginData;

import java.io.Serializable;

public class VehicleDetailResponse implements Serializable {

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("data")
    @Expose
    private VehicleResponseData data;

    public VehicleResponseData getData() {
        return data;
    }

    public void setData(VehicleResponseData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
