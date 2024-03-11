package com.royalenfield.reprime.models.request.proxy.service.paymentgateway;

public class PaymentGatewayRequest {

    private String txnAmount;

    private String caseId;

    private String currencyType;

    private String guid;

    public PaymentGatewayRequest(String txnAmount, String caseId, String currencyType,
                                 String guid) {
        this.txnAmount = txnAmount;
        this.caseId=caseId;
        this.currencyType=currencyType;
        this.guid = guid;
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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
