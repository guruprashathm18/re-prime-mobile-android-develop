package com.royalenfield.reprime.models.response.web.booking;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class PartsDatum {

    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("configid")
    @Expose
    private String configid;
    @SerializedName("paymentRefNo")
    @Expose
    private String paymentRefNo;
    @SerializedName("accessoriesCode")
    @Expose
    private String accessoriesCode;
    @SerializedName("accessoriesName")
    @Expose
    private String accessoriesName;
    @SerializedName("accessoriesQty")
    @Expose
    private Double accessoriesQty;
    @SerializedName("accessoreisPrice")
    @Expose
    private Double accessoreisPrice;
    @SerializedName("gmapartnumber")
    @Expose
    private String gmapartnumber;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("partdescription")
    @Expose
    private String partdescription;
    @SerializedName("qty")
    @Expose
    private String qty;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getConfigid() {
        return configid;
    }

    public void setConfigid(String configid) {
        this.configid = configid;
    }

    public String getPaymentRefNo() {
        return paymentRefNo;
    }

    public void setPaymentRefNo(String paymentRefNo) {
        this.paymentRefNo = paymentRefNo;
    }

    public String getAccessoriesCode() {
        return accessoriesCode;
    }

    public void setAccessoriesCode(String accessoriesCode) {
        this.accessoriesCode = accessoriesCode;
    }

    public String getAccessoriesName() {
        return accessoriesName;
    }

    public void setAccessoriesName(String accessoriesName) {
        this.accessoriesName = accessoriesName;
    }

    public Double getAccessoriesQty() {
        return accessoriesQty;
    }

    public void setAccessoriesQty(Double accessoriesQty) {
        this.accessoriesQty = accessoriesQty;
    }

    public Double getAccessoreisPrice() {
        return accessoreisPrice;
    }

    public void setAccessoreisPrice(Double accessoreisPrice) {
        this.accessoreisPrice = accessoreisPrice;
    }

    public String getGmapartnumber() {
        return gmapartnumber;
    }

    public void setGmapartnumber(String gmapartnumber) {
        this.gmapartnumber = gmapartnumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPartdescription() {
        return partdescription;
    }

    public void setPartdescription(String partdescription) {
        this.partdescription = partdescription;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

}