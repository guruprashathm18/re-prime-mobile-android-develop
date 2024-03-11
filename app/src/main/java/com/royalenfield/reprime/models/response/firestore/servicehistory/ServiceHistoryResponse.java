package com.royalenfield.reprime.models.response.firestore.servicehistory;

import java.io.Serializable;

public class ServiceHistoryResponse implements Serializable {

    private String BillAmount = "";
    private String BranchId = "";
    private String ChassisNo = "";
    private String InvoiceDate = "";
    private String LastUpdatedOn = "";
    private String RegNo = "";
    private String ServiceInvoiceNum = "";
    private String UserID = "";
    private String User_MobileNumber = "";

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String branchId) {
        BranchId = branchId;
    }

    public String getChassisNo() {
        return ChassisNo;
    }

    public void setChassisNo(String chassisNo) {
        ChassisNo = chassisNo;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getLastUpdatedOn() {
        return LastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        LastUpdatedOn = lastUpdatedOn;
    }

    public String getRegNo() {
        return RegNo;
    }

    public void setRegNo(String regNo) {
        RegNo = regNo;
    }

    public String getServiceInvoiceNum() {
        return ServiceInvoiceNum;
    }

    public void setServiceInvoiceNum(String serviceInvoiceNum) {
        ServiceInvoiceNum = serviceInvoiceNum;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUser_MobileNumber() {
        return User_MobileNumber;
    }

    public void setUser_MobileNumber(String user_MobileNumber) {
        User_MobileNumber = user_MobileNumber;
    }
}
