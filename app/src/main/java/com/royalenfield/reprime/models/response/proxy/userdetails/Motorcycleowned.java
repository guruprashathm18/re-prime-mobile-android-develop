package com.royalenfield.reprime.models.response.proxy.userdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Motorcycleowned implements Serializable {

    @SerializedName("engineNumber")
    @Expose
    private String engineNumber;
    @SerializedName("motorcycle_id")
    @Expose
    private String motorcycleId;
    @SerializedName("motorcycle_name")
    @Expose
    private String motorcycleName;
    @SerializedName("purchase_date")
    @Expose
    private String purchaseDate;
    @SerializedName("registration_number")
    @Expose
    private String registrationNumber;
    @SerializedName("userID")
    @Expose
    private String userID;

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getMotorcycleId() {
        return motorcycleId;
    }

    public void setMotorcycleId(String motorcycleId) {
        this.motorcycleId = motorcycleId;
    }

    public String getMotorcycleName() {
        return motorcycleName;
    }

    public void setMotorcycleName(String motorcycleName) {
        this.motorcycleName = motorcycleName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}