package com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleServiceProgressListResponse {

    @SerializedName("appointmentNumber")
    @Expose
    private String appointmentNumber;
    @SerializedName("appointmentStatus")
    @Expose
    private String appointmentStatus;
    @SerializedName("isServiceInProgress")
    @Expose
    private String isServiceInProgress;
    @SerializedName("appointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("JobcardStatus")
    @Expose
    private Object jobcardStatus;
    @SerializedName("DealerInfo")
    @Expose
    private String dealerInfo;
    @SerializedName("ModelName")
    @Expose
    private String modelName;
    @SerializedName("Paybleamount")
    @Expose
    private String paybleamount;
    @SerializedName("PickupAddres")
    @Expose
    private String pickupAddres;
    @SerializedName("CustomerRemarks")
    @Expose
    private String customerRemarks;
    @SerializedName("registrationNumber")
    @Expose
    private String registrationNumber;
    @SerializedName("serviceBookingType")
    @Expose
    private String serviceBookingType;
    private String pickUpTime;

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(String appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getIsServiceInProgress() {
        return isServiceInProgress;
    }

    public void setIsServiceInProgress(String isServiceInProgress) {
        this.isServiceInProgress = isServiceInProgress;
    }

    public Object getJobcardStatus() {
        return jobcardStatus;
    }

    public void setJobcardStatus(Object jobcardStatus) {
        this.jobcardStatus = jobcardStatus;
    }

    public String getDealerInfo() {
        return dealerInfo;
    }

    public void setDealerInfo(String dealerInfo) {
        this.dealerInfo = dealerInfo;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getPaybleamount() {
        return paybleamount;
    }

    public void setPaybleamount(String paybleamount) {
        this.paybleamount = paybleamount;
    }

    public String getPickupAddres() {
        return pickupAddres;
    }

    public void setPickupAddres(String pickupAddres) {
        this.pickupAddres = pickupAddres;
    }

    public String getCustomerRemarks() {
        return customerRemarks;
    }

    public void setCustomerRemarks(String customerRemarks) {
        this.customerRemarks = customerRemarks;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }
    public String getServiceBookingType() {
        return serviceBookingType;
    }

    public void setServiceBookingType(String serviceBookingType) {
        this.serviceBookingType = serviceBookingType;
    }

}
