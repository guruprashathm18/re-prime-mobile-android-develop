package com.royalenfield.reprime.ui.userdatavalidation.popup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.AddressInfoRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.ContactInformationRequestModel;

import java.io.Serializable;

public class SwapConfigRequestModel implements Serializable {

    @SerializedName("oldPhoneNo")
    @Expose
    private String oldPhoneNo;
    @SerializedName("newPhoneNo")
    @Expose
    private String newPhoneNo;

    public String getOldPhoneNo() {
        return oldPhoneNo;
    }

    public void setOldPhoneNo(String oldPhoneNo) {
        this.oldPhoneNo = oldPhoneNo;
    }

    public String getNewPhoneNo() {
        return newPhoneNo;
    }

    public void setNewPhoneNo(String newPhoneNo) {
        this.newPhoneNo = newPhoneNo;
    }
}
