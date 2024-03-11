package com.royalenfield.reprime.models.request.web.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleDataRequestModel implements Serializable {

    @SerializedName("engineNo")
    @Expose
    private String engineNo;

    @SerializedName("vehicleModelName")
    @Expose
    private String modelName;

    @SerializedName("previousRegistrationNumber")
    @Expose
    private String previousRegistrationNumber;

    @SerializedName("removeBike")
    @Expose
    private boolean removeBike;

    @SerializedName("updatedRegistrationNumber")
    @Expose
    private String updatedRegistrationNumber;


    @SerializedName("chassisNo")
    @Expose
    private String chassisNo;



    public VehicleDataRequestModel(String engineNo, String modelName
            , String previousRegistrationNumber, boolean removeBike, String updatedRegistrationNumber,String chessisNumber) {
        this.engineNo = engineNo;
        this.modelName = modelName;
        this.previousRegistrationNumber = previousRegistrationNumber;
        this.removeBike = removeBike;
        this.updatedRegistrationNumber = updatedRegistrationNumber;
        this.chassisNo=chessisNumber;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getEngineNo() {
        return this.engineNo;
    }

    public String getModelName() {
        return this.modelName;
    }

    public String getPreviousRegistrationNumber() {
        return this.previousRegistrationNumber;
    }

    public boolean isRemoveBike() {
        return this.removeBike;
    }

    public String getUpdatedRegistrationNumber() {
        return this.updatedRegistrationNumber;
    }
}
