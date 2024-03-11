package com.royalenfield.reprime.models.request.proxy.service.vehicledetails;

public class VehicleDetailsRequest {

    private String RegistrationNo;

    private String ChassisNo;

    private String EngineNo;

    private String Mobileno;

    public VehicleDetailsRequest(String RegistrationNo, String ChassisNo, String EngineNo, String mobileno) {
        this.RegistrationNo = RegistrationNo;
        this.ChassisNo = ChassisNo;
        this.EngineNo = EngineNo;
        this.Mobileno = mobileno;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    public String getChassisNo() {
        return ChassisNo;
    }

    public void setChassisNo(String chassisNo) {
        ChassisNo = chassisNo;
    }

    public String getEngineNo() {
        return EngineNo;
    }

    public void setEngineNo(String engineNo) {
        EngineNo = engineNo;
    }

    public String getMobileno() {
        return Mobileno;
    }

    public void setMobileno(String mobileno) {
        Mobileno = mobileno;
    }

}
