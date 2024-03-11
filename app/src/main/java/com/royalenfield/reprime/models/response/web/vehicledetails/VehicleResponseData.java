package com.royalenfield.reprime.models.response.web.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleResponseData implements Serializable {

    @SerializedName("vehicleInfo")
    @Expose
    private VehicleData[] mVehicleInfo;

    public VehicleData[] getVehicleInfo() {
        return mVehicleInfo;
    }

    public void setVehicleInfo(VehicleData[] mVehicleInfo) {
        this.mVehicleInfo = mVehicleInfo;
    }
}
