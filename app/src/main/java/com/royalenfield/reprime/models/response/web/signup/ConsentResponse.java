package com.royalenfield.reprime.models.response.web.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.response.proxy.servicebooking.ServiceBookingResponse;

import java.util.List;
import java.util.Objects;

public class ConsentResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Objects data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Objects getData() {
        return data;
    }

    public void setData(Objects data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}