package com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PrimaryData implements Serializable {

    @SerializedName("callingCode")
    @Expose
    private String callingCode;
    @SerializedName("isVerified")
    @Expose
    private boolean isVerified;
    @SerializedName("number")
    @Expose
    private String number;
    private final static long serialVersionUID = 5517440659823429480L;

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
