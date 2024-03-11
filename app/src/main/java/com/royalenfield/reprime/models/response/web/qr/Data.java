package com.royalenfield.reprime.models.response.web.qr;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Data {
        @SerializedName("partName")
        @Expose
        private String partName;
        @SerializedName("partCode")
        @Expose
        private String partCode;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("dealerName")
        @Expose
        private String dealerName;
        @SerializedName("mfgDate")
        @Expose
        private String mfgDate;
        @SerializedName("scanCount")
        @Expose
        private Integer scanCount;
    @SerializedName("displayMessage")
    @Expose
    private String displayMessage;

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getPartName() {
            return partName;
        }

        public void setPartName(String partName) {
            this.partName = partName;
        }

        public String getPartCode() {
            return partCode;
        }

        public void setPartCode(String partCode) {
            this.partCode = partCode;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDealerName() {
            return dealerName;
        }

        public void setDealerName(String dealerName) {
            this.dealerName = dealerName;
        }

        public String getMfgDate() {
            return mfgDate;
        }

        public void setMfgDate(String mfgDate) {
            this.mfgDate = mfgDate;
        }

        public Integer getScanCount() {
            return scanCount;
        }

        public void setScanCount(Integer scanCount) {
            this.scanCount = scanCount;
        }

    }
