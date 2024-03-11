package com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateContactRequestModel implements Serializable {

    @SerializedName("callingCode")
    @Expose
    private String callingCode;

    @SerializedName("secondary")
    @Expose
    private String secondaryNumber;

    @SerializedName("emergency")
    @Expose
    private String emergencyNumber;

    @SerializedName("email")
    @Expose
    private String email;

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public String getSecondary() {
        return secondaryNumber;
    }

    public void setSecondary(String secondary) {
        this.secondaryNumber = secondary;
    }

    public String getEmergency() {
        return emergencyNumber;
    }

    public void setEmergency(String emergency) {
        this.emergencyNumber = emergency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
