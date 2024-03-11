package com.royalenfield.reprime.models.response.web.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.response.web.otp.Data;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleResponseData;

import java.io.Serializable;

public class VerifyAccountResponse implements Serializable {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
