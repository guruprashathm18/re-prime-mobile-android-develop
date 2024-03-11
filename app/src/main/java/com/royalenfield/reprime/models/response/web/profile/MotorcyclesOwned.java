package com.royalenfield.reprime.models.response.web.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MotorcyclesOwned {

    @SerializedName("bikeId")
    @Expose
    private String bikeId;
    @SerializedName("vehicleNo")
    @Expose
    private Object vehicleNo;

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public Object getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(Object vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

}
