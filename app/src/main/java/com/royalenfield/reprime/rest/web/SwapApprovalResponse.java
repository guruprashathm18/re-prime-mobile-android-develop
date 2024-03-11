package com.royalenfield.reprime.rest.web;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SwapApprovalResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("code")
    private int code;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public int getCode() {
        return code;
    }
}
