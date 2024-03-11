package com.royalenfield.reprime.models.request.proxy.service.servicebooking;

public class ServiceBookingStatus {

    private String CaseNo;

    private String Mobile;

    private String CancelAppointment;


    public ServiceBookingStatus(String CaseNo, String Mobile, String cancelAppointment) {
        this.CaseNo = CaseNo;
        this.Mobile = Mobile;
        this.CancelAppointment = cancelAppointment;
    }

    public String getCaseNo() {
        return CaseNo;
    }

    public void setCaseNo(String caseNo) {
        CaseNo = caseNo;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCancelAppointment() {
        return CancelAppointment;
    }

    public void setCancelAppointment(String cancelAppointment) {
        CancelAppointment = cancelAppointment;
    }

}
