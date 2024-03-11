package com.royalenfield.reprime.models.request.proxy.firebase;

/**
 * Request for sending devicetoken to server
 */
public class FirebaseTokenRequest {

    private String guid;
    private String phoneNumber;
    private String deviceToken;
    private String deviceType;
    private String deviceKey;
    private String appVersion;
    private String appBuildNumber;
    private String osVersion;

    public FirebaseTokenRequest(String phoneNo, String devicetoken
            , String devicetype, String devicekey, String gUID,String appVersion,String appBuildNumber,String osVersion) {
        this.phoneNumber = phoneNo;
        this.deviceToken = devicetoken;
        this.deviceType = devicetype;
        this.deviceKey = devicekey;
        this.guid = gUID;
        this.appVersion=appVersion;
        this.appBuildNumber=appBuildNumber;
        this.osVersion=osVersion;
    }

    public String getAppBuildNumber() {
        return appBuildNumber;
    }

    public void setAppBuildNumber(String appBuildNumber) {
        this.appBuildNumber = appBuildNumber;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getGUid() {
        return guid;
    }

    public String getOsVersion() {
        return osVersion;
    }

}
