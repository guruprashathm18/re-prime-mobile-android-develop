package com.royalenfield.reprime.models.response.proxy.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DMSVehicleDetailsResponse implements Serializable {

    @SerializedName("ActiveCustomerName")
    @Expose
    private String activeCustomerName;
    @SerializedName("AvgOdometerReading")
    @Expose
    private String avgOdometerReading;
    @SerializedName("ChassisNo")
    @Expose
    private String chassisNo;
    @SerializedName("DateOfMfg")
    @Expose
    private String dateOfMfg;
    @SerializedName("EngineNo")
    @Expose
    private String engineNo;
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("InsuranceRenewalDate")
    @Expose
    private String insuranceRenewalDate;
    @SerializedName("InvoiceDate")
    @Expose
    private String invoiceDate;
    @SerializedName("InvoiceNo")
    @Expose
    private String invoiceNo;
    @SerializedName("MobileNo")
    @Expose
    private Object mobileNo;
    @SerializedName("ModelCode")
    @Expose
    private String modelCode;
    @SerializedName("Modelname")
    @Expose
    private String modelname;
    @SerializedName("PolicyNum")
    @Expose
    private String policyNum;
    @SerializedName("PurchaseDate")
    @Expose
    private String purchaseDate;
    @SerializedName("RSA")
    @Expose
    private Object rSA;
    @SerializedName("RSAEndDate")
    @Expose
    private String rSAEndDate;
    @SerializedName("RSAStartDate")
    @Expose
    private String rSAStartDate;
    @SerializedName("RegistrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("SellingDealerID")
    @Expose
    private String sellingDealerID;
    @SerializedName("UserID")
    @Expose
    private Object userID;

    public String getActiveCustomerName() {
        return activeCustomerName;
    }

    public void setActiveCustomerName(String activeCustomerName) {
        this.activeCustomerName = activeCustomerName;
    }

    public String getAvgOdometerReading() {
        return avgOdometerReading;
    }

    public void setAvgOdometerReading(String avgOdometerReading) {
        this.avgOdometerReading = avgOdometerReading;
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

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getInsuranceRenewalDate() {
        return insuranceRenewalDate;
    }

    public void setInsuranceRenewalDate(String insuranceRenewalDate) {
        this.insuranceRenewalDate = insuranceRenewalDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Object getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Object mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Object getRSA() {
        return rSA;
    }

    public void setRSA(Object rSA) {
        this.rSA = rSA;
    }

    public String getRSAEndDate() {
        return rSAEndDate;
    }

    public void setRSAEndDate(String rSAEndDate) {
        this.rSAEndDate = rSAEndDate;
    }

    public String getRSAStartDate() {
        return rSAStartDate;
    }

    public void setRSAStartDate(String rSAStartDate) {
        this.rSAStartDate = rSAStartDate;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getSellingDealerID() {
        return sellingDealerID;
    }

    public void setSellingDealerID(String sellingDealerID) {
        this.sellingDealerID = sellingDealerID;
    }

    public Object getUserID() {
        return userID;
    }

    public void setUserID(Object userID) {
        this.userID = userID;
    }
}
