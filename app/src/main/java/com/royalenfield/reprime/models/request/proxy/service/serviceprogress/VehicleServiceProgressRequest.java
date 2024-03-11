package com.royalenfield.reprime.models.request.proxy.service.serviceprogress;

public class VehicleServiceProgressRequest {

    private String Mobileno;

    public VehicleServiceProgressRequest(String mobileNo){
        this.Mobileno=mobileNo;
    }

    public String getMobileNo() {
        return Mobileno;
    }

    public void setMobileNo(String mobileNo) {
        this.Mobileno = mobileNo;
    }

}
