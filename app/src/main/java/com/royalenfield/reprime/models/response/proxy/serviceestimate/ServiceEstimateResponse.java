package com.royalenfield.reprime.models.response.proxy.serviceestimate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceEstimateResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("totalEstimate")
    @Expose
    private String totalEstimate;
    @SerializedName("estimateDate")
    @Expose
    private String estimateDate;

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTotalEstimate() {
        return totalEstimate;
    }

    public void setTotalEstimate(String totalEstimate) {
        this.totalEstimate = totalEstimate;
    }

    public String getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(String estimateDate) {
        this.estimateDate = estimateDate;
    }
}
