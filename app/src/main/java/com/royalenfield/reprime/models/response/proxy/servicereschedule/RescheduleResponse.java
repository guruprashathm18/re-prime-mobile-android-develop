package com.royalenfield.reprime.models.response.proxy.servicereschedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RescheduleResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("response")
    @Expose
    private List<ServiceAppointmentRescheduleResponse> response = null;

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

    public List<ServiceAppointmentRescheduleResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ServiceAppointmentRescheduleResponse> response) {
        this.response = response;
    }

}
