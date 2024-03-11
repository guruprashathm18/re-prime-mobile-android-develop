package com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddContactRequestModel {

    @SerializedName("callingCode")
    @Expose
    private String callingCode;
    @SerializedName("secondary")
    @Expose
    private String secondaryPhoneNumber;
    @SerializedName("emergency")
    @Expose
    private String emergencyNumber;

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getCallingCode() {
        return callingCode;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }
}
