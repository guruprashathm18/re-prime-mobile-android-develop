package com.royalenfield.reprime.models.request.web.otap;

public class UpdateTripperDeviceApiRequest {

    private String serialNumber;
    private String OSVersion;
    private String brickStatus;
    private String tripperState;
    private String additionalInfo1;

    public UpdateTripperDeviceApiRequest(String serialNumber, String OSVersion, String brickStatus, String tripperState){
        this.serialNumber = serialNumber;
        this.OSVersion = OSVersion;
        this.brickStatus = brickStatus;
        this.tripperState = tripperState;
    }
    public UpdateTripperDeviceApiRequest(String serialNumber, String OSVersion, String brickStatus, String tripperState,String additionalInfo1){
        this.serialNumber = serialNumber;
        this.OSVersion = OSVersion;
        this.brickStatus = brickStatus;
        this.tripperState = tripperState;
        this.additionalInfo1 = additionalInfo1;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getTripperState() {
        return tripperState;
    }

    public void setTripperState(String tripperState) {
        this.tripperState = tripperState;
    }

    public String getAdditionalInfo1() {
        return additionalInfo1;
    }

    public void setAdditionalInfo1(String additionalInfo1) {
        this.additionalInfo1 = additionalInfo1;
    }
}
