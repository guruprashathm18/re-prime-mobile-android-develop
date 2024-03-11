package com.royalenfield.reprime.models.response.web.tbtauthentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TbtAuthResponse {


    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("message")
    @Expose
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
