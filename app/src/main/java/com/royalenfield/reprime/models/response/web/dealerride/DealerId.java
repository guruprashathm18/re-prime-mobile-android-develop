package com.royalenfield.reprime.models.response.web.dealerride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerId {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Landmark")
    @Expose
    private String landmark;
    @SerializedName("DealerName")
    @Expose
    private String dealerName;
    @SerializedName("DealerImage")
    @Expose
    private Object dealerImage;
    @SerializedName("PrimaryImage_mobile")
    @Expose
    private Object primaryImageMobile;
    @SerializedName("PrimaryImage")
    @Expose
    private Object primaryImage;
    @SerializedName("Thumbnail_Image")
    @Expose
    private Object thumbnailImage;
    @SerializedName("Cover_image_mobile")
    @Expose
    private Object coverImageMobile;
    @SerializedName("Cover_image")
    @Expose
    private Object coverImage;
    @SerializedName("MainPhoneNo")
    @Expose
    private String mainPhoneNo;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Pincode")
    @Expose
    private Integer pincode;
    @SerializedName("AddressLine3")
    @Expose
    private String addressLine3;
    @SerializedName("AddressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Object getDealerImage() {
        return dealerImage;
    }

    public void setDealerImage(Object dealerImage) {
        this.dealerImage = dealerImage;
    }

    public Object getPrimaryImageMobile() {
        return primaryImageMobile;
    }

    public void setPrimaryImageMobile(Object primaryImageMobile) {
        this.primaryImageMobile = primaryImageMobile;
    }

    public Object getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(Object primaryImage) {
        this.primaryImage = primaryImage;
    }

    public Object getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(Object thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public Object getCoverImageMobile() {
        return coverImageMobile;
    }

    public void setCoverImageMobile(Object coverImageMobile) {
        this.coverImageMobile = coverImageMobile;
    }

    public Object getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Object coverImage) {
        this.coverImage = coverImage;
    }

    public String getMainPhoneNo() {
        return mainPhoneNo;
    }

    public void setMainPhoneNo(String mainPhoneNo) {
        this.mainPhoneNo = mainPhoneNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

}
