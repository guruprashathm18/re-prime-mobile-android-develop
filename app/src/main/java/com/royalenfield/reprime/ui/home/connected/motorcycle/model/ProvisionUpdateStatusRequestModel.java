package com.royalenfield.reprime.ui.home.connected.motorcycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProvisionUpdateStatusRequestModel {
    @SerializedName("vin")
    @Expose
    private String vin;

    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;

    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("authorizedStatus")
    @Expose
    private boolean authorizedStatus;

    public ProvisionUpdateStatusRequestModel(String vin,String guid, String phoneNo, boolean authorizedStatus) {
        this.vin = vin;
        this.phoneNo = phoneNo;
        this.authorizedStatus = authorizedStatus;
        this.guid=guid;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(boolean authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
