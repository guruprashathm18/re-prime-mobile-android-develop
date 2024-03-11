package com.royalenfield.reprime.ui.motorcyclehealthalert.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleAlertResponse {
    @SerializedName("data")
    @Expose
    private VehiclAlertData data;

    public VehiclAlertData getData() {
        return data;
    }

    public void setData(VehiclAlertData data) {
        this.data = data;
    }
}
