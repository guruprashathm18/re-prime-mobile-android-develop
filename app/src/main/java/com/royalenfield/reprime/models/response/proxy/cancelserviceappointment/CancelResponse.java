package com.royalenfield.reprime.models.response.proxy.cancelserviceappointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("response")
    @Expose
    private ServiceAppointmentCancelResponse response;

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

    public ServiceAppointmentCancelResponse getResponse() {
        return response;
    }

    public void setResponse(ServiceAppointmentCancelResponse response) {
        this.response = response;
    }

}
