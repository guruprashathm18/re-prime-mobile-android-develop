package com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddAddressRequestModel implements Serializable {

    @SerializedName("contactInformation")
    @Expose
    private ContactInformationRequestModel contactInformationRequestModel;
    @SerializedName("addressInfo")
    @Expose
    private AddressInfoRequestModel addressInfoRequestModel;
    @SerializedName("default_shipping")
    @Expose
    private boolean defaultShipping;
    @SerializedName("default_billing")
    @Expose
    private boolean defaultBilling;

    public void setContactInformationRequestModel(ContactInformationRequestModel contactInformationRequestModel) {
        this.contactInformationRequestModel = contactInformationRequestModel;
    }

    public void setAddressInfoRequestModel(AddressInfoRequestModel addressInfoRequestModel) {
        this.addressInfoRequestModel = addressInfoRequestModel;
    }

    public void setDefaultShipping(boolean defaultShipping) {
        this.defaultShipping = defaultShipping;
    }

    public void setDefaultBilling(boolean defaultBilling) {
        this.defaultBilling = defaultBilling;
    }

    public ContactInformationRequestModel getContactInformationRequestModel() {
        return contactInformationRequestModel;
    }

    public AddressInfoRequestModel getAddressInfoRequestModel() {
        return addressInfoRequestModel;
    }

    public boolean isDefaultShipping() {
        return defaultShipping;
    }

    public boolean isDefaultBilling() {
        return defaultBilling;
    }
}
