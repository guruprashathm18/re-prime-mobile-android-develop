package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalValidations {
    @SerializedName("pincode")
    @Expose
    private Integer pincode;
    @SerializedName("phoneNumber")
    @Expose
    private Integer phoneNumber;

    @SerializedName("minPhoneNum")
    @Expose
    private Integer minPhoneNum;
    @SerializedName("minPin")
    @Expose
    private Integer minPin;

    public Integer getMinPhoneNum() {
        return minPhoneNum;
    }

    public void setMinPhoneNum(Integer minPhoneNum) {
        this.minPhoneNum = minPhoneNum;
    }

    public Integer getMinPin() {
        return minPin;
    }

    public void setMinPin(Integer minPin) {
        this.minPin = minPin;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
