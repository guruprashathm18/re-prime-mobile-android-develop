package com.royalenfield.reprime.ui.home.service.paymentgateway;

/**
 * Class file containing BillDesk parameters
 */
public class BillDeskParams {

    private static BillDeskParams mBillDeskParams = new BillDeskParams();
    //ToDo  need to fetch from BillDesk apis during integration
    private String merchantId = "EICHERAPP";
    private String securityId = "eicherapp";
    private String RU = "https://www.billdesk.com";
    private String ChecksumKey = "TKC3WOXwmx7V";
    private String billDeskUrl = "https://pgi.billdesk.com/pgidsk/PGIMerchantPayment";

    private BillDeskParams() {
    }

    public static BillDeskParams getBillDeskParamsInstances() {
        return mBillDeskParams;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public String getRU() {
        return RU;
    }

    public void setRU(String RU) {
        this.RU = RU;
    }

    public String getChecksumKey() {
        return ChecksumKey;
    }

    public void setChecksumKey(String checksumKey) {
        ChecksumKey = checksumKey;
    }

    public String getBillDeskUrl() {
        return billDeskUrl;
    }

    public void setBillDeskUrl(String billDeskUrl) {
        this.billDeskUrl = billDeskUrl;
    }

}
