package com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmergencyData {

    @SerializedName("callingCode")
    @Expose
    private String callingCode;
    @SerializedName("number")
    @Expose
    private String number;
    private final static long serialVersionUID = -4034919998678452055L;

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
