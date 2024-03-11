package com.royalenfield.reprime.models.response.firestore.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVehicleDetailsResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
