package com.royalenfield.reprime.rest.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteUserContactResponse {

    @SerializedName("code")
    @Expose
    private long code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("success")
    @Expose
    private boolean success;

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}
