package com.royalenfield.reprime.ui.home.service.paymentgateway;

public class PaymentGatewayRequest {

    private String txnAmount;

    private String caseId;

    private String currencyType;

    public PaymentGatewayRequest(String txnAmount, String caseId, String currencyType) {
        this.txnAmount = txnAmount;
        this.caseId=caseId;
        this.currencyType=currencyType;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
