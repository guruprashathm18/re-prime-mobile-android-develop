package com.royalenfield.reprime.ui.userdatavalidation.popup.presenter;

import java.io.Serializable;

public class LastUpdatedUserDataModel implements Serializable {

    private String lastUpdatedEmail;
    private LastUpdateAddressModel lastUpdatedAddress;
    private String emergencyPhoneNumber;
    private String secondaryPhoneNumber;
    private String emergencyCountryCode;
    private String secondaryCountryCode;


    public void setLastUpdatedEmail(String lastUpdatedEmail) {
        this.lastUpdatedEmail = lastUpdatedEmail;
    }

    public String getLastUpdatedEmail() {
        return lastUpdatedEmail;
    }

    public LastUpdateAddressModel getLastUpdatedAddress() {
        return lastUpdatedAddress;
    }

    public void setLastUpdatedAddress(LastUpdateAddressModel lastUpdatedAddress) {
        this.lastUpdatedAddress = lastUpdatedAddress;
    }

    public String getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public String getEmergencyCountryCode() {
        return emergencyCountryCode;
    }

    public void setEmergencyCountryCode(String emergencyCountryCode) {
        this.emergencyCountryCode = emergencyCountryCode;
    }

    public String getSecondaryCountryCode() {
        return secondaryCountryCode;
    }

    public void setSecondaryCountryCode(String secondaryCountryCode) {
        this.secondaryCountryCode = secondaryCountryCode;
    }
}
