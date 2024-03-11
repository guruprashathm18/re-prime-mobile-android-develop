package com.royalenfield.reprime.models.request.web.otap;

public class OtapGetDeviceinfoApiRequest {

    private String guid;
    private String deviceToken;

    public  OtapGetDeviceinfoApiRequest(String guid, String deviceToken){
        this.guid = guid;
        this.deviceToken = deviceToken;
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


}
