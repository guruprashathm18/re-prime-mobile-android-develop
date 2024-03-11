
package com.royalenfield.reprime.models.response.firestore.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserVehicle {

    @SerializedName("engineNo")
    @Expose
    private String engineNo;
    @SerializedName("purchaseDate")
    @Expose
    private String purchaseDate;
    @SerializedName("vehicleModelCode")
    @Expose
    private String vehicleModelCode;
    @SerializedName("registrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("chassisNo")
    @Expose
    private String chassisNo;
    @SerializedName("msdynName")
    @Expose
    private String msdynName;
    @SerializedName("dateOfMfg")
    @Expose
    private String dateOfMfg;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("vehicleModelName")
    @Expose
    private String vehicleModelName;
    @SerializedName("alternateNo")
    @Expose
    private String alternateNo;
    @SerializedName("vehicleStatus")
    @Expose
    private String vehicleStatus;

    @SerializedName("vehicleUpdateStatus")
    @Expose
    private String vehicleUpdateStatus;

    @SerializedName("connectedMotorcycle")
    @Expose
    private boolean connectedMotorcycle;


    public String getVehicleUpdateStatus() {
        return vehicleUpdateStatus;
    }

    public void setVehicleUpdateStatus(String vehicleUpdateStatus) {
        this.vehicleUpdateStatus = vehicleUpdateStatus;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getVehicleModelCode() {
        return vehicleModelCode;
    }

    public void setVehicleModelCode(String vehicleModelCode) {
        this.vehicleModelCode = vehicleModelCode;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getMsdynName() {
        return msdynName;
    }

    public void setMsdynName(String msdynName) {
        this.msdynName = msdynName;
    }

    public String getDateOfMfg() {
        return dateOfMfg;
    }

    public void setDateOfMfg(String dateOfMfg) {
        this.dateOfMfg = dateOfMfg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName;
    }

    public String getAlternateNo() {
        return alternateNo;
    }

    public void setAlternateNo(String alternateNo) {
        this.alternateNo = alternateNo;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public boolean getConnectedMotorcycle() {
        return connectedMotorcycle;
    }

    public void setConnectedMotorcycle(boolean connectedMotorcycle) {
        this.connectedMotorcycle = connectedMotorcycle;
    }
}
