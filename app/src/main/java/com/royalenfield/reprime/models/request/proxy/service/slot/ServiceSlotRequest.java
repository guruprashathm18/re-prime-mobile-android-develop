package com.royalenfield.reprime.models.request.proxy.service.slot;


public class ServiceSlotRequest {

    private String appointmentdate;

    private String branchID;

    private boolean isDummySlots;

    public ServiceSlotRequest(String appointmentdate, String branchID, boolean isDummySlots) {
        this.appointmentdate = appointmentdate;
        this.branchID = branchID;
        this.isDummySlots = isDummySlots;
    }


    public String getAppointmentdate() {
        return appointmentdate;
    }

    public void setAppointmentdate(String appointmentdate) {
        this.appointmentdate = appointmentdate;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

}
