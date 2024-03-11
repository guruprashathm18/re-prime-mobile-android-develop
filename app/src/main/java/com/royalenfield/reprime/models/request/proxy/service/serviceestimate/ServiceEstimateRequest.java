package com.royalenfield.reprime.models.request.proxy.service.serviceestimate;

public class ServiceEstimateRequest {

    private String RegistrationNo;

    public ServiceEstimateRequest(String RegistrationNo) {
        this.RegistrationNo = RegistrationNo;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }
}
