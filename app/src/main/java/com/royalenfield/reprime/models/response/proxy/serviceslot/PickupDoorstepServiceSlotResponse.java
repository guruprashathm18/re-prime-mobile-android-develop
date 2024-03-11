package com.royalenfield.reprime.models.response.proxy.serviceslot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PickupDoorstepServiceSlotResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<PickupandDoorstepServiceSlot> getResponse() {
        return response;
    }

    public void setResponse(List<PickupandDoorstepServiceSlot> response) {
        this.response = response;
    }

    @SerializedName("response")
    @Expose
    private List<PickupandDoorstepServiceSlot> response = null;

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