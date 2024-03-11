package com.royalenfield.reprime.models.response.web.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinanceModel {
    @SerializedName("financerInterestRate")
    @Expose
    private String financerInterestRate;
    @SerializedName("financerLoanAmount")
    @Expose
    private String financerLoanAmount ;
    @SerializedName("financerLoanId")
    @Expose
    private String financerLoanId;
    @SerializedName("financerName")
    @Expose
    private String financerName;
    @SerializedName("financerStatus")
    @Expose
    private String financerStatus;
    @SerializedName("financerTenure")
    @Expose
    private String financerTenure;
    @SerializedName("financerDownPayment")
    @Expose
    private String financerDownPayment;
    @SerializedName("financerEstimatedEmi")
    @Expose
    private String financerEmi;

    @SerializedName("financerProcessingFee")
    @Expose
    private String financerProcessingFee;

    public String getFinancerDownPayment() {
        return financerDownPayment;
    }

    public void setFinancerDownPayment(String financerDownPayment) {
        this.financerDownPayment = financerDownPayment;
    }

    public String getFinancerEmi() {
        return financerEmi;
    }

    public void setFinancerEmi(String financerEmi) {
        this.financerEmi = financerEmi;
    }

    public String getFinancerProcessingFee() {
        return financerProcessingFee;
    }

    public void setFinancerProcessingFee(String financerProcessingFee) {
        this.financerProcessingFee = financerProcessingFee;
    }

    public String getFinancerInterestRate() {
        return financerInterestRate;
    }

    public void setFinancerInterestRate(String financerInterestRate) {
        this.financerInterestRate = financerInterestRate;
    }

    public String getFinancerLoanAmount() {
        return financerLoanAmount;
    }

    public void setFinancerLoanAmount(String financerLoanAmount) {
        this.financerLoanAmount = financerLoanAmount;
    }

    public String getFinancerLoanId() {
        return financerLoanId;
    }

    public void setFinancerLoanId(String financerLoanId) {
        this.financerLoanId = financerLoanId;
    }

    public String getFinancerName() {
        return financerName;
    }

    public void setFinancerName(String financerName) {
        this.financerName = financerName;
    }

    public String getFinancerStatus() {
        return financerStatus;
    }

    public void setFinancerStatus(String financerStatus) {
        this.financerStatus = financerStatus;
    }

    public String getFinancerTenure() {
        return financerTenure;
    }

    public void setFinancerTenure(String financerTenure) {
        this.financerTenure = financerTenure;
    }
}
