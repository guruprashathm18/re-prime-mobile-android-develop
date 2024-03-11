package com.royalenfield.reprime.models.response.proxy.serviceslot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PickupandDoorstepServiceSlot {

    @SerializedName("date")
    @Expose
    private String availableDate;
    @SerializedName("pickupAvailability")
    @Expose
    private Boolean pickupAvailability;
    @SerializedName("doorStepAvailability")
    @Expose
    private Boolean doorStepAvailability;

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public Boolean getPickupAvailability() {
        return pickupAvailability;
    }

    public void setPickupAvailability(Boolean pickupAvailability) {
        this.pickupAvailability = pickupAvailability;
    }

    public Boolean getDoorStepAvailability() {
        return doorStepAvailability;
    }

    public void setDoorStepAvailability(Boolean doorStepAvailability) {
        this.doorStepAvailability = doorStepAvailability;
    }


}
