package com.royalenfield.reprime.models.response.proxy.servicebooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("response")
    @Expose
    private List<ServiceBookingResponse> response = null;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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

    public List<ServiceBookingResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ServiceBookingResponse> response) {
        this.response = response;
    }

}
