package com.royalenfield.reprime.models.response.web.booking;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("paymentCaseId")
    @Expose
    private String paymentCaseId;
    @SerializedName("billDeskTrnsactionId")
    @Expose
    private String billDeskTrnsactionId;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;
    @SerializedName("bookingPostedDate")
    @Expose
    private String bookingPostedDate;
    @SerializedName("partsData")
    @Expose
    private List<PartsDatum> partsData = null;
    @SerializedName("apparelData")
    @Expose
    private List<PartsDatum> apparelData = null;

    @SerializedName("dealerSource")
    @Expose
    private String dealerSource;
    @SerializedName("modelCode")
    @Expose
    private String modelCode;


    @SerializedName("modelName")
    @Expose
    private String modelName;

    @SerializedName("tentativeDeliveryDays")
    @Expose
    private String tentativeDeliveryDays;

    @SerializedName("financerData")
    @Expose
    private List<FinanceModel> financerDetails;

    @SerializedName("paymentModeDetails")
    @Expose
    private PaymentModeModel paymentModeDetails;

    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;

    @SerializedName("showOrderTracking")
    @Expose
    private Boolean showOrderTracking;


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentModeModel getPaymentModeDetails() {
        return paymentModeDetails;
    }

    public void setPaymentModeDetails(PaymentModeModel paymentModeDetails) {
        this.paymentModeDetails = paymentModeDetails;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getPaymentCaseId() {
        return paymentCaseId;
    }

    public void setPaymentCaseId(String paymentCaseId) {
        this.paymentCaseId = paymentCaseId;
    }

    public String getBillDeskTrnsactionId() {
        return billDeskTrnsactionId;
    }

    public void setBillDeskTrnsactionId(String billDeskTrnsactionId) {
        this.billDeskTrnsactionId = billDeskTrnsactionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingPostedDate() {
        return bookingPostedDate;
    }

    public void setBookingPostedDate(String bookingPostedDate) {
        this.bookingPostedDate = bookingPostedDate;
    }

    public List<PartsDatum> getPartsData() {
        return partsData;
    }

    public void setPartsData(List<PartsDatum> partsData) {
        this.partsData = partsData;
    }

    public String getDealerSource() {
        return dealerSource;
    }

    public void setDealerSource(String dealerSource) {
        this.dealerSource = dealerSource;
    }


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTentativeDeliveryDays() {
        return tentativeDeliveryDays;
    }

    public void setTentativeDeliveryDays(String tentativeDeliveryDays) {
        this.tentativeDeliveryDays = tentativeDeliveryDays;
    }


    public List<FinanceModel> getFinancerDetails() {
        return financerDetails;
    }

    public void setFinancerDetails(List<FinanceModel> financerDetails) {
        this.financerDetails = financerDetails;
    }

    public List<PartsDatum> getApparelData() {
        return apparelData;
    }

    public void setApparelData(List<PartsDatum> apparelData) {
        this.apparelData = apparelData;
    }

    public Boolean getShowOrderTracking() {
        return showOrderTracking;
    }

    public void setShowOrderTracking(Boolean showOrderTracking) {
        this.showOrderTracking = showOrderTracking;
    }
}