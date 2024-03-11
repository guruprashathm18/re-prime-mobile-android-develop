package com.royalenfield.reprime.models.request.web.vehicledetails;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleDetailRequest {

    @SerializedName("appId")
    @Expose
    private int appId;
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("loggedInUserMobileNo")
    @Expose
    private String loggedInUserMobileNo;
    @SerializedName("loggedInUserName")
    @Expose
    private String loggedInUserName;
    @SerializedName("loggedInUserEmail")
    @Expose
    private String loggedInUserEmail;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLoggedInUserMobileNo() {
        return loggedInUserMobileNo;
    }

    public void setLoggedInUserMobileNo(String loggedInUserMobileNo) {
        this.loggedInUserMobileNo = loggedInUserMobileNo;
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    public void setLoggedInUserName(String loggedInUserName) {
        this.loggedInUserName = loggedInUserName;
    }

    public String getLoggedInUserEmail() {
        return loggedInUserEmail;
    }

    public void setLoggedInUserEmail(String loggedInUserEmail) {
        this.loggedInUserEmail = loggedInUserEmail;
    }

}
