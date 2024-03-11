package com.royalenfield.reprime.ui.userdatavalidation.otp.otpinteractor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubmitOtpResponseModel implements Serializable {

    @SerializedName("code")
    @Expose
    int code;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("success")
    @Expose
    boolean success;

    @SerializedName("data")
    @Expose
    Object data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }
}
