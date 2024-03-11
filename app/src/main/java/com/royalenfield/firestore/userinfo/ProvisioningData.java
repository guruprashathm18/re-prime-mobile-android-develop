package com.royalenfield.firestore.userinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProvisioningData {

    @SerializedName("new_primary_no_details")
    @Expose
    private PrimaryNoDetails newPrimaryNoDetails;
    @SerializedName("new_emergency_no_details")
    @Expose
    private EmergencyNoDetails newEmergencyNoDetails;
    @SerializedName("bikes_owned")
    @Expose
    private List<String> bikesOwned;


    public PrimaryNoDetails getNewPrimaryNoDetails() {
        return newPrimaryNoDetails;
    }

    public void setNewPrimaryNoDetails(PrimaryNoDetails newPrimaryNoDetails) {
        this.newPrimaryNoDetails = newPrimaryNoDetails;
    }

    public EmergencyNoDetails getNewEmergencyNoDetails() {
        return newEmergencyNoDetails;
    }

    public void setNewEmergencyNoDetails(EmergencyNoDetails newEmergencyNoDetails) {
        this.newEmergencyNoDetails = newEmergencyNoDetails;
    }

    public List<String> getBikesOwned() {
        return bikesOwned;
    }

    public void setBikesOwned(List<String> bikesOwned) {
        this.bikesOwned = bikesOwned;
    }

    public static class EmergencyNoDetails {

        @SerializedName("emergency_no")
        @Expose
        private String emergencyNo;
        @SerializedName("calling_code")
        @Expose
        private String callingCode;

        public String getEmergencyNo() {
            return emergencyNo;
        }

        public void setEmergencyNo(String emergencyNo) {
            this.emergencyNo = emergencyNo;
        }

        public String getCallingCode() {
            return callingCode;
        }

        public void setCallingCode(String callingCode) {
            this.callingCode = callingCode;
        }


    }
    public static class PrimaryNoDetails {

        @SerializedName("primary_no")
        @Expose
        private String primaryNo;
        @SerializedName("calling_code")
        @Expose
        private String callingCode;

        public String getPrimaryNo() {
            return primaryNo;
        }

        public void setPrimaryNo(String primaryNo) {
            this.primaryNo = primaryNo;
        }

        public String getCallingCode() {
            return callingCode;
        }

        public void setCallingCode(String callingCode) {
            this.callingCode = callingCode;
        }

    }
    }