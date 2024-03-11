package com.royalenfield.reprime.models.request.web.vehicledetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UpdateVehicleDetailRequest implements Serializable {

    @SerializedName("appId")
    @Expose
    private int appId;

    @SerializedName("guid")
    @Expose
    private String guid;

    @SerializedName("loggedInUserMobileNo")
    @Expose
    private String mobileNo;

    @SerializedName("loggedInUserName")
    @Expose
    private String loggedInUserName;

    @SerializedName("loggedInUserEmail")
    @Expose
    private String loggedInUserEmail;


    private List<VehicleDataRequestModel> vehicleList;

    public UpdateVehicleDetailRequest(int appId, String mobileNo, List<VehicleDataRequestModel> vehicleList) {

        this.appId = appId;
        this.mobileNo = mobileNo;
        this.vehicleList = vehicleList;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public List<VehicleDataRequestModel> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<VehicleDataRequestModel> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
