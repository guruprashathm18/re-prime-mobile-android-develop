package com.royalenfield.reprime.models.response.web.ourworldnews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OurWorldNewsResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<OurWorldNewsEntity> data = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OurWorldNewsEntity> getData() {
        return data;
    }

    public void setData(List<OurWorldNewsEntity> data) {
        this.data = data;
    }

}