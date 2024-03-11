
package com.royalenfield.reprime.models.request.web.vehicleonboarding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyVehicleRequest {

    @SerializedName("chassisNumber")
    @Expose
    private String chassisNumber;
    @SerializedName("registrationNumber")
    @Expose
    private String registrationNumber;
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


    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
