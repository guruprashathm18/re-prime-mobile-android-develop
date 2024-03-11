package com.royalenfield.reprime.ui.home.service.paymentgateway;

/**
 * CheckSumMessage :: forms checksum message and calculates checkSumValue
 */
public class CheckSumMessage {
    /**
     * forms checksum message and returns checkSumValue
     *
     * @param merchantId
     * @param customerId
     * @param filler_1
     * @param txnAmount
     * @param bankID
     * @param filler_2
     * @param filler_3
     * @param currencyType
     * @param itemCode
     * @param typeField1
     * @param securityId
     * @param filter_4
     * @param filter_5
     * @param typeField2
     * @param additionalInfo1
     * @param additionalInfo2
     * @param additionalInfo3
     * @param additionalInfo4
     * @param additionalInfo5
     * @param additionalInfo6
     * @param additionalInfo7
     * @param returnURl
     * @param checkSumKey
     * @return checkSumValue
     */
    //TODO need to replace customerId,securityId with the actual customerId and securityId provided by BillDesk during integration
    public static String formCheckSumMessage(String merchantId, String customerId, String filler_1, float txnAmount, String bankID, String filler_2, String filler_3, String currencyType, String itemCode,
                                             String typeField1, String securityId, String filter_4, String filter_5, String typeField2, String additionalInfo1, String additionalInfo2, String additionalInfo3, String additionalInfo4,
                                             String additionalInfo5, String additionalInfo6, String additionalInfo7, String returnURl, String checkSumKey) {

        String checkSumMsg = merchantId + "|" + customerId + "|" + filler_1 + "|" + txnAmount + "|" + bankID +
                "|" + filler_2 + "|" + filler_3 + "|" + currencyType + "|" + itemCode + "|" + typeField1 + "|" + securityId +
                "|" + filter_4 + "|" + filter_5 + "|" + typeField2 + "|" + additionalInfo1 + "|" + additionalInfo2 +
                "|" + additionalInfo3 + "|" + additionalInfo4 + "|" + additionalInfo5 + "|" + additionalInfo6 + "|" + additionalInfo7 + "|" + returnURl + "";

        String checkSumValue = checkSumMsg + "|" + HMAC.HmacSHA256(checkSumMsg, checkSumKey);
        return checkSumValue;
    }


}
