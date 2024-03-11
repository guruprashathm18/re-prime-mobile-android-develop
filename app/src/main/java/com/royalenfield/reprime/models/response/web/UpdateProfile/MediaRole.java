package com.royalenfield.reprime.models.response.web.UpdateProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaRole {

    @SerializedName("status")
    @Expose
    private Object status;

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

}
