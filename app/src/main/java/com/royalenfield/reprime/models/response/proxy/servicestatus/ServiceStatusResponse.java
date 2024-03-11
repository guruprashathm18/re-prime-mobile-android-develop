package com.royalenfield.reprime.models.response.proxy.servicestatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;

public class ServiceStatusResponse {

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("response")
    @Expose
    private ServiceBookingRequest response;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ServiceBookingRequest getResponse() {
        return response;
    }

    public void setResponse(ServiceBookingRequest response) {
        this.response = response;
    }
}
