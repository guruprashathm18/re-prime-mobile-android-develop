package com.royalenfield.reprime.models.response.web.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public class ResetPasswordResponse {
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
