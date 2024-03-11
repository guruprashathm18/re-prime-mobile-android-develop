package com.royalenfield.reprime.models.response.web.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentModeModel {

    @SerializedName("chequeNumber")
    @Expose
    private String chequeNumber;
    @SerializedName("ddNumber")
    @Expose
    private String ddNumber ;
    @SerializedName("cardType")
    @Expose
    private String cardType;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("paymentDate")
    @Expose
    private String paymentDate;
    @SerializedName("dateOfCheque")
    @Expose
    private String dateOfCheque;
    @SerializedName("dateOfDd")
    @Expose
    private String dateOfDd;
    @SerializedName("bankReferenceNumber")
    @Expose
    private String bankReferenceNumber;
    @SerializedName("approvalCode")
    @Expose
    private String approvalCode;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getDdNumber() {
        return ddNumber;
    }

    public void setDdNumber(String ddNumber) {
        this.ddNumber = ddNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDateOfCheque() {
        return dateOfCheque;
    }

    public void setDateOfCheque(String dateOfCheque) {
        this.dateOfCheque = dateOfCheque;
    }

    public String getDateOfDd() {
        return dateOfDd;
    }

    public void setDateOfDd(String dateOfDd) {
        this.dateOfDd = dateOfDd;
    }

    public String getBankReferenceNumber() {
        return bankReferenceNumber;
    }

    public void setBankReferenceNumber(String bankReferenceNumber) {
        this.bankReferenceNumber = bankReferenceNumber;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
