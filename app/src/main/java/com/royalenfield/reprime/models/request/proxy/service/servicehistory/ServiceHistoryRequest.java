package com.royalenfield.reprime.models.request.proxy.service.servicehistory;

public class ServiceHistoryRequest {

    private String ChassisNo;

    private String EngineNo;

    private String RegistrationNo;

    private String UserID;

    public ServiceHistoryRequest(String chassisNo, String engineNo, String registrationNo, String userID) {
        this.ChassisNo = chassisNo;
        this.EngineNo = engineNo;
        this.RegistrationNo = registrationNo;
        this.UserID = userID;
    }

    public String getChassisNo() {
        return ChassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.ChassisNo = chassisNo;
    }

    public String getEngineNo() {
        return EngineNo;
    }

    public void setEngineNo(String engineNo) {
        this.EngineNo = engineNo;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.RegistrationNo = registrationNo;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }
}
