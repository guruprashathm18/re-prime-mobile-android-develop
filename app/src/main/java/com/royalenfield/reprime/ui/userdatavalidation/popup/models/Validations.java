package com.royalenfield.reprime.ui.userdatavalidation.popup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Validations {

    @SerializedName("pincode")
    @Expose
    private Pincode pincode;
    @SerializedName("phoneNumber")
    @Expose
    private Pincode phoneNumber;

    public Pincode getPincode() {
        return pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public Pincode getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Pincode phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}