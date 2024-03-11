
package com.royalenfield.reprime.models.response.web.vehicleonboarding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data  implements Serializable {

    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("chassisNo")
    @Expose
    private String chassisNo;
    @SerializedName("dateOfMfg")
    @Expose
    private String dateOfMfg;
    @SerializedName("engineNo")
    @Expose
    private String engineNo;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("purchaseDate")
    @Expose
    private String purchaseDate;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("vehicleModelCode")
    @Expose
    private String modelCode;
    @SerializedName("vehicleModelName")
    @Expose
    private String modelName;
    @SerializedName("registrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("invoiceNo")
    @Expose
    private Object invoiceNo;
    @SerializedName("alternateNo")
    @Expose
    private Object alternateNo;

    @SerializedName("vehiclePurchaseDateLessThan30Days")
    @Expose
    private boolean vehiclePurchaseDateLessThan30Days;

    public boolean isVehiclePurchaseDateLessThan30Days() {
        return vehiclePurchaseDateLessThan30Days;
    }

    public void setVehiclePurchaseDateLessThan30Days(boolean vehiclePurchaseDateLessThan30Days) {
        this.vehiclePurchaseDateLessThan30Days = vehiclePurchaseDateLessThan30Days;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getDateOfMfg() {
        return dateOfMfg;
    }

    public void setDateOfMfg(String dateOfMfg) {
        this.dateOfMfg = dateOfMfg;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public Object getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(Object invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Object getAlternateNo() {
        return alternateNo;
    }

    public void setAlternateNo(Object alternateNo) {
        this.alternateNo = alternateNo;
    }

}
