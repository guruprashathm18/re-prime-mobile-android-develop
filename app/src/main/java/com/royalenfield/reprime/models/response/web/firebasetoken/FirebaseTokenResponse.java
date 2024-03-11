package com.royalenfield.reprime.models.response.web.firebasetoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirebaseTokenResponse {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("status")
    @Expose
    private int status;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
