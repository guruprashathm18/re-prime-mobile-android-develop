package com.royalenfield.reprime.models.response.proxy.serviceslot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceSlotListResponse {


    @SerializedName("startTimeSlot")
    @Expose
    private String startTimeSlot;
    @SerializedName("endTimeSlot")
    @Expose
    private String endTimeSlot;
    @SerializedName("availability")
    @Expose
    private Boolean availability;
    @SerializedName("resourceId")
    @Expose
    private String resourceId;
    @SerializedName("branchId")
    @Expose
    private String branchId;
    @SerializedName("date")
    @Expose
    private String date;

    private List<String> listOfResourceId;

    public String getStartTimeSlot() {
        return startTimeSlot;
    }

    public void setStartTimeSlot(String startTimeSlot) {
        this.startTimeSlot = startTimeSlot;
    }

    public String getEndTimeSlot() {
        return endTimeSlot;
    }

    public void setEndTimeSlot(String endTimeSlot) {
        this.endTimeSlot = endTimeSlot;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getListOfResourceId() {
        return listOfResourceId;
    }

    public void setListOfResourceId(List<String> listOfResourceId) {
        this.listOfResourceId = listOfResourceId;
    }
}
