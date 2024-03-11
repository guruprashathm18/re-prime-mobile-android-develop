package com.royalenfield.reprime.models.response.web.otap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OtapSaveDeviceinfoApiRequest {
    /*@SerializedName("devices")
    @Expose
    private ArrayList<DeviceInfoApiItems> devices;*/
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    //serialNumber - Tripper UniqueId
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("userGUID")
    @Expose
    private String userGUID;
    //OSVersion- Tripper OS vrsion
    @SerializedName("OSVersion")
    @Expose
    private String OSVersion;
    @SerializedName("brickStatus")
    @Expose
    private String brickStatus;
   //deviceOS - Android
    @SerializedName("deviceOS")
    @Expose
    private String deviceOS;
    @SerializedName("name")
    @Expose
    private String name;
    //tripperID - MacID
    @SerializedName("tripperID")
    @Expose
    private String tripperID;
    //tripperState - Active
    @SerializedName("tripperState")
    @Expose
    private String tripperState;
    //uuid - For Ios MacID - pass empty always
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("additionalInfo1")
    @Expose
    private String additionalInfo1;
    @SerializedName("additionalInfo2")
    @Expose
    private String additionalInfo2;

    public OtapSaveDeviceinfoApiRequest(String deviceToken,String serialNumber,String userGUID,String OSVersion,String brickStatus,String deviceOS
    ,String name,String tripperID,String tripperState,String uuid,String additionalInfo1,String additionalInfo2){
        this.deviceToken = deviceToken;
        this.serialNumber =serialNumber;
        this.userGUID =userGUID;
        this.OSVersion =OSVersion;
        this.brickStatus =brickStatus;
        this.deviceOS =deviceOS;
        this.name =name;
        this.tripperID =tripperID;
        this.tripperState =tripperState;
        this.uuid =uuid;
        this.additionalInfo1 =additionalInfo1;
        this.additionalInfo2 =additionalInfo2;
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

    public String getBrickStatus() {
        return brickStatus;
    }

    public void setBrickStatus(String brickStatus) {
        this.brickStatus = brickStatus;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTripperID() {
        return tripperID;
    }

    public void setTripperID(String tripperID) {
        this.tripperID = tripperID;
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
