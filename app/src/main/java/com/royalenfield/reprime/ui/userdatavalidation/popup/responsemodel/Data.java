package com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("addressID")
    @Expose
    private String addressID;
    @SerializedName("contactInformation")
    @Expose
    private ContactInformation contactInformation;
    @SerializedName("addressInfo")
    @Expose
    private AddressInfo addressInfo;
    @SerializedName("default_shipping")
    @Expose
    private Boolean defaultShipping;
    @SerializedName("default_billing")
    @Expose
    private Boolean defaultBilling;
    private final static long serialVersionUID = -5592809576959163808L;

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public Boolean getDefaultShipping() {
        return defaultShipping;
    }

    public void setDefaultShipping(Boolean defaultShipping) {
        this.defaultShipping = defaultShipping;
    }

    public Boolean getDefaultBilling() {
        return defaultBilling;
    }

    public void setDefaultBilling(Boolean defaultBilling) {
        this.defaultBilling = defaultBilling;
    }

}

