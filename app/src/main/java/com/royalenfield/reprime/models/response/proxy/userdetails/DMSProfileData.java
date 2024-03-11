package com.royalenfield.reprime.models.response.proxy.userdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DMSProfileData implements Serializable {

    @SerializedName("data")
    @Expose
    private UserData data;
    @SerializedName("result")
    @Expose
    private String result;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
