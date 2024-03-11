package com.royalenfield.reprime.models.response.web.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.response.web.login.User;

import java.io.Serializable;

public class VehicleData implements Serializable {

    @SerializedName("userName")
    @Expose
    private String mUserName;

    @SerializedName("chassisNo")
    @Expose
    private String mChaissisNo;

    @SerializedName("dateOfMfg")
    @Expose
    private String mDateOfMfg;

    @SerializedName("engineNo")
    @Expose
    private String mEngineNo;

    @SerializedName("id")
    @Expose
    private String mId;

    @SerializedName("purchaseDate")
    @Expose
    private String mPurchaseDate;

    @SerializedName("mobileNo")
    @Expose
    private String mMobileNo;

    @SerializedName("vehicleModelCode")
    @Expose
    private String mModelCode;

    @SerializedName("vehicleModelName")
    @Expose
    private String mModelName;

    @SerializedName("registrationNo")
    @Expose
    private String mRegistrationNo;

    @SerializedName("invoiceNo")
    @Expose
    private String mInvoiceNo;

    @SerializedName("alternateNo")
    @Expose
    private String mAlternateNo;

    @SerializedName("connectedMotorcycle")
    @Expose
    private boolean connectedMotorcycle;


    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getChaissisNo() {
        return mChaissisNo;
    }

    public void setChaissisNo(String mChaissisNo) {
        this.mChaissisNo = mChaissisNo;
    }

    public String getDateOfMfg() {
        return mDateOfMfg;
    }

    public void setDateOfMfg(String mDateOfMfg) {
        this.mDateOfMfg = mDateOfMfg;
    }

    public String getEngineNo() {
        return mEngineNo;
    }

    public void setEngineNo(String mEngineNo) {
        this.mEngineNo = mEngineNo;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(String mPurchaseDate) {
        this.mPurchaseDate = mPurchaseDate;
    }

    public String getMobileNo() {
        return mMobileNo;
    }

    public void setMobileNo(String mMobileNo) {
        this.mMobileNo = mMobileNo;
    }

    public String getModelCode() {
        return mModelCode;
    }

    public void setModelCode(String mModelCode) {
        this.mModelCode = mModelCode;
    }

    public String getModelName() {
        return mModelName;
    }

    public void setModelName(String mModelName) {
        this.mModelName = mModelName;
    }

    public String getRegistrationNo() {
        return mRegistrationNo;
    }

    public void setRegistrationNo(String mRegistrationNo) {
        this.mRegistrationNo = mRegistrationNo;
    }

    public String getInvoiceNo() {
        return mInvoiceNo;
    }

    public void setInvoiceNo(String mInvoiceNo) {
        this.mInvoiceNo = mInvoiceNo;
    }

    public String getAlternateNo() {
        return mAlternateNo;
    }

    public void setAlternateNo(String mAlternateNo) {
        this.mAlternateNo = mAlternateNo;
    }

    public boolean isConnectedMotorcycle() {
        return connectedMotorcycle;
    }

    public void setConnectedMotorcycle(boolean connectedMotorcycle) {
        this.connectedMotorcycle = connectedMotorcycle;
    }
}
