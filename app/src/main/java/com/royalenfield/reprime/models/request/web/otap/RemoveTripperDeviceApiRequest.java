package com.royalenfield.reprime.models.request.web.otap;

public class RemoveTripperDeviceApiRequest {

    private String guid;
    private String deviceToken;
    private String serialNumber;

    public RemoveTripperDeviceApiRequest(String guid, String deviceToken, String serialNumber){
        this.guid = guid;
        this.deviceToken = deviceToken;
        this.serialNumber = serialNumber;
    }
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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
}
