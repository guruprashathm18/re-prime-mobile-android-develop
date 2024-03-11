package com.royalenfield.reprime.models.request.proxy.service.reschedule;

public class ServiceAppointmentRescheduleRequest {

    private String AppointmentId;
    private String AppointmentDate;
    private String AttachmentKey;
    private String CustomerRemarks;
    private String DropAddress;
    private String SlotID;
    private String IsPickUpDrop;
    private String PickUpAddress;
    private String ServiceType;
    private String Creation_Source;
    private String BranchID;
    private String Mobile;
    private String RegNo;
    private String BookingReschedule;
    private boolean isDummySlots;


    public ServiceAppointmentRescheduleRequest(String appointmentId, String appointmentDate, String attachmentKey,
                                               String customerRemarks, String dropAddress, String slotID,
                                               String isPickUpDrop, String pickUpAddress, String serviceType,
                                               String creation_Source, String branchID, String mobile, String regNo,
                                               String bookingReschedule,boolean isdummyslots) {
        AppointmentId = appointmentId;
        AppointmentDate = appointmentDate;
        AttachmentKey = attachmentKey;
        CustomerRemarks = customerRemarks;
        DropAddress = dropAddress;
        SlotID = slotID;
        IsPickUpDrop = isPickUpDrop;
        PickUpAddress = pickUpAddress;
        ServiceType = serviceType;
        Creation_Source = creation_Source;
        BranchID = branchID;
        Mobile = mobile;
        RegNo = regNo;
        BookingReschedule = bookingReschedule;
        isDummySlots = isdummyslots;
    }

    public String getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        AppointmentId = appointmentId;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getAttachmentKey() {
        return AttachmentKey;
    }

    public void setAttachmentKey(String attachmentKey) {
        AttachmentKey = attachmentKey;
    }

    public String getCustomerRemarks() {
        return CustomerRemarks;
    }

    public void setCustomerRemarks(String customerRemarks) {
        CustomerRemarks = customerRemarks;
    }

    public String getDropAddress() {
        return DropAddress;
    }

    public void setDropAddress(String dropAddress) {
        DropAddress = dropAddress;
    }

    public String getSlotID() {
        return SlotID;
    }

    public void setSlotID(String slotID) {
        SlotID = slotID;
    }

    public String getIsPickUpDrop() {
        return IsPickUpDrop;
    }

    public void setIsPickUpDrop(String isPickUpDrop) {
        IsPickUpDrop = isPickUpDrop;
    }

    public String getPickUpAddress() {
        return PickUpAddress;
    }

    public void setPickUpAddress(String pickUpAddress) {
        PickUpAddress = pickUpAddress;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getCreation_Source() {
        return Creation_Source;
    }

    public void setCreation_Source(String creation_Source) {
        Creation_Source = creation_Source;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getRegNo() {
        return RegNo;
    }

    public void setRegNo(String regNo) {
        RegNo = regNo;
    }

    public String getBookingReschedule() {
        return BookingReschedule;
    }

    public void setBookingReschedule(String bookingReschedule) {
        BookingReschedule = bookingReschedule;
    }
    public boolean isDummySlots() {
        return isDummySlots;
    }

    public void setDummySlots(boolean dummySlots) {
        isDummySlots = dummySlots;
    }

}
