package com.royalenfield.reprime.models.response.web.ourworldevent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OurWorldEventResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<OurWorldEventEntity> data = null;

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

    public List<OurWorldEventEntity> getData() {
        return data;
    }

    public void setData(List<OurWorldEventEntity> data) {
        this.data = data;
    }

}
