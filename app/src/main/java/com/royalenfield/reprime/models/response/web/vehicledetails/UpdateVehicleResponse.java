package com.royalenfield.reprime.models.response.web.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateVehicleResponse implements Serializable {

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("data")
    @Expose
    private String response;

    @SerializedName("error")
    @Expose
    private boolean error;

    public int getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }

    public boolean isError() {
        return error;
    }
}
