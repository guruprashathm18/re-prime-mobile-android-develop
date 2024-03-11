package com.royalenfield.reprime.models.response.web.qr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestQR {
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("gpslocation")
    @Expose
    private String gpslocation;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGpslocation() {
        return gpslocation;
    }

    public void setGpslocation(String gpslocation) {
        this.gpslocation = gpslocation;
    }
}
