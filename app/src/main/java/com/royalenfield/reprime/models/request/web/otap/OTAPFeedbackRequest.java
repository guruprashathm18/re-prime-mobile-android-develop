package com.royalenfield.reprime.models.request.web.otap;

public class OTAPFeedbackRequest {

    private String firmwareId;

    private String deviceId;

    private int status;

    public OTAPFeedbackRequest(String firmwareId, String deviceId, int status) {
        this.firmwareId = firmwareId;
        this.deviceId = deviceId;
        this.status = status;
    }

    public String getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(String firmwareId) {
        this.firmwareId = firmwareId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
