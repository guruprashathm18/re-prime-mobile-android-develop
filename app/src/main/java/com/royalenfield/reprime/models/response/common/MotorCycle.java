package com.royalenfield.reprime.models.response.common;

import com.royalenfield.reprime.models.response.proxy.userdetails.Motorcycleowned;
import com.royalenfield.reprime.models.response.proxy.vehicledetails.DMSVehicleDetailsResponse;

import java.io.Serializable;

/**
 * @author BOP1KOR on 1/7/2019.
 */

public class MotorCycle extends Motorcycleowned implements Serializable {
    private String engineNumber;
    private String motorcycleId;
    private String motorcycleName;
    private String purchaseDate;
    private String registrationNumber;
    private String userID;
    private DMSVehicleDetailsResponse vehicleDetails;


    public MotorCycle(String engineNumber, String motorcycleId, String motorcycleName, String purchaseDate, String registrationNumber, String userID) {
        this.engineNumber = engineNumber;
        this.motorcycleId = motorcycleId;
        this.motorcycleName = motorcycleName;
        this.purchaseDate = purchaseDate;
        this.registrationNumber = registrationNumber;
        this.userID = userID;
    }

    @Override
    public String getEngineNumber() {
        return engineNumber;
    }

    @Override
    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    @Override
    public String getMotorcycleId() {
        return motorcycleId;
    }

    @Override
    public void setMotorcycleId(String motorcycleId) {
        this.motorcycleId = motorcycleId;
    }

    @Override
    public String getMotorcycleName() {
        return motorcycleName;
    }

    @Override
    public void setMotorcycleName(String motorcycleName) {
        this.motorcycleName = motorcycleName;
    }

    @Override
    public String getPurchaseDate() {
        return purchaseDate;
    }

    @Override
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public DMSVehicleDetailsResponse getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(DMSVehicleDetailsResponse vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }
}
