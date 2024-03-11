package com.royalenfield.reprime.models.response.proxy.serviceslot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceSlotResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<ServiceSlotListResponse> data = null;

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

    public List<ServiceSlotListResponse> getData() {
        return data;
    }

    public void setData(List<ServiceSlotListResponse> data) {
        this.data = data;
    }
}