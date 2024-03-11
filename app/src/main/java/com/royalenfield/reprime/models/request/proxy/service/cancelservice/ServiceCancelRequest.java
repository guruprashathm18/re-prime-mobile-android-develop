package com.royalenfield.reprime.models.request.proxy.service.cancelservice;

public class ServiceCancelRequest {

    private String CaseNo;

    private String Mobile;

    private String CancelAppointment;

    private boolean isDummySlots;

    public ServiceCancelRequest(String CaseNo, String Mobile, String cancelAppointment) {
        this.CaseNo = CaseNo;
        this.Mobile = Mobile;
        this.CancelAppointment = cancelAppointment;
    }
    public ServiceCancelRequest(String CaseNo, String Mobile, String cancelAppointment,boolean isdummyslots) {
        this.CaseNo = CaseNo;
        this.Mobile = Mobile;
        this.CancelAppointment = cancelAppointment;
        this.isDummySlots = isdummyslots;
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
    public boolean isDummySlots() {
        return isDummySlots;
    }

    public void setDummySlots(boolean dummySlots) {
        isDummySlots = dummySlots;
    }
}
