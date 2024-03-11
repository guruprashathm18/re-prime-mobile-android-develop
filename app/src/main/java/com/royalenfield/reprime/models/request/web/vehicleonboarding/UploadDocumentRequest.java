
package com.royalenfield.reprime.models.request.web.vehicleonboarding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadDocumentRequest {

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
    private String vehicleModelCode;
    @SerializedName("vehicleModelName")
    @Expose
    private String vehicleModelName;
    @SerializedName("registrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("invoiceNo")
    @Expose
    private Object invoiceNo;
    @SerializedName("alternateNo")
    @Expose
    private Object alternateNo;

    @SerializedName("appId")
    @Expose
    private int appId;
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("loggedInUserMobileNo")
    @Expose
    private String loggedInUserMobileNo;
    @SerializedName("loggedInUserName")
    @Expose
    private String loggedInUserName;
    @SerializedName("loggedInUserEmail")
    @Expose
    private String loggedInUserEmail;


    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLoggedInUserMobileNo() {
        return loggedInUserMobileNo;
    }

    public void setLoggedInUserMobileNo(String loggedInUserMobileNo) {
        this.loggedInUserMobileNo = loggedInUserMobileNo;
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    public void setLoggedInUserName(String loggedInUserName) {
        this.loggedInUserName = loggedInUserName;
    }

    public String getLoggedInUserEmail() {
        return loggedInUserEmail;
    }

    public void setLoggedInUserEmail(String loggedInUserEmail) {
        this.loggedInUserEmail = loggedInUserEmail;
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

    public String getVehicleModelCode() {
        return vehicleModelCode;
    }

    public void setVehicleModelCode(String vehicleModelCode) {
        this.vehicleModelCode = vehicleModelCode;
    }

    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName;
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
