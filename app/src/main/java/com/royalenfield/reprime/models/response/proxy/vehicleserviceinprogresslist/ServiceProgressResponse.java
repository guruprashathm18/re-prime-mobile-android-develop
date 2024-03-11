package com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceProgressResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<VehicleServiceProgressListResponse> data;

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

    public List<VehicleServiceProgressListResponse> getData() {
        return data;
    }

    public void setData(List<VehicleServiceProgressListResponse> data) {
        this.data = data;
    }

}
