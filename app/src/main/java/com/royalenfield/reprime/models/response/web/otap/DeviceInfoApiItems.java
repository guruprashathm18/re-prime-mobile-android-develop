package com.royalenfield.reprime.models.response.web.otap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceInfoApiItems {
    @SerializedName("brickStatus")
    @Expose
    private String brickStatus;
    @SerializedName("tripperID")
    @Expose
    private String tripperID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created")
    @Expose
    private String lastLoginTime;
    @SerializedName("deviceOS")
    @Expose
    private String mobileDeviceOS;
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("userGUID")
    @Expose
    private String userGUID;
    @SerializedName("OSVersion")
    @Expose
    private String OSVersion;
    @SerializedName("tripperState")
    @Expose
    private String tripperState;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("additionalInfo1")
    @Expose
    private String additionalInfo1;
    @SerializedName("additionalInfo2")
    @Expose
    private String additionalInfo2;



    public DeviceInfoApiItems(String mobiledeviceos, String brickStatus, String tripperID, String name,String deviceToken,String serialNumber,String userGUID,String OSVersion, String lastLoginTime
    ,String tripperState,String uuid,String additionalInfo1,String additionalInfo2 ){
        this.brickStatus = brickStatus;
        this.tripperID = tripperID;
        this.name = name;
        this.lastLoginTime = lastLoginTime;
        this.mobileDeviceOS = mobiledeviceos;
        this.deviceToken = deviceToken;
        this.serialNumber = serialNumber;
        this.userGUID = userGUID;
        this.OSVersion = OSVersion;
        this.tripperState = tripperState;
        this.uuid = uuid;
        this.additionalInfo1 = additionalInfo1;
        this.additionalInfo2 = additionalInfo2;
    }
    public String getMobileDeviceOS() {
        return mobileDeviceOS;
    }

    public void setMobileDeviceOS(String mobileDeviceOS) {
        this.mobileDeviceOS = mobileDeviceOS;
    }
    public String getBrickStatus() {
        return brickStatus;
    }

    public void setBrickStatus(String brickStatus) {
        this.brickStatus = brickStatus;
    }

    public String getTripperID() {
        return tripperID;
    }

    public void setTripperID(String tripperID) {
        this.tripperID = tripperID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUserGUID() {
        return userGUID;
    }

    public void setUserGUID(String userGUID) {
        this.userGUID = userGUID;
    }

    public String getOSVersion() {
        return OSVersion;
    }

    public void setOSVersion(String OSVersion) {
        this.OSVersion = OSVersion;
    }

    public String getTripperState() {
        return tripperState;
    }

    public void setTripperState(String tripperState) {
        this.tripperState = tripperState;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAdditionalInfo1() {
        return additionalInfo1;
    }

    public void setAdditionalInfo1(String additionalInfo1) {
        this.additionalInfo1 = additionalInfo1;
    }

    public String getAdditionalInfo2() {
        return additionalInfo2;
    }

    public void setAdditionalInfo2(String additionalInfo2) {
        this.additionalInfo2 = additionalInfo2;
    }
}
