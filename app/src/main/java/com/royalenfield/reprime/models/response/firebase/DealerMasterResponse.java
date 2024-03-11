package com.royalenfield.reprime.models.response.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerMasterResponse implements Parcelable {

    @SerializedName("dealerId")
    @Expose
    private String dealerId;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("latlng")
    @Expose
    private String latlng;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("dealerName")
    @Expose
    private String dealerName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("addressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("addressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("branchCode")
    @Expose
    private String branchCode;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("dealerSource")  //dealerSource will be E/D
    @Expose
    private String dealerSource;
    @SerializedName("isService")
    @Expose
    private Boolean isService;
    @SerializedName("doorStepServiceAvailable")
    @Expose
    private String doorStepServiceAvailable;
    @SerializedName("pickUpServiceAvailable")
    @Expose
    private String pickUpServiceAvailable;
    @SerializedName("branchTypeIdentifier")
    @Expose
    private String branchTypeIdentifier;

    private Boolean isPickupandDropAvailable = true;

    private Boolean isDoorStepServiceAvailable =  true;


    public final static Parcelable.Creator<DealerMasterResponse> CREATOR = new Creator<DealerMasterResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DealerMasterResponse createFromParcel(Parcel in) {
            return new DealerMasterResponse(in);
        }

        public DealerMasterResponse[] newArray(int size) {
            return (new DealerMasterResponse[size]);
        }

    };

    protected DealerMasterResponse(Parcel in) {
        this.dealerId = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.pincode = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.emailId = ((String) in.readValue((String.class.getClassLoader())));
        this.dealerName = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.branchName = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine2 = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine1 = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.branchCode = ((String) in.readValue((String.class.getClassLoader())));
        this.distance = ((String) in.readValue((String.class.getClassLoader())));
        this.dealerSource = ((String) in.readValue((String.class.getClassLoader())));
        this.isService = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.latlng = ((String) in.readValue((String.class.getClassLoader())));
        this.doorStepServiceAvailable = ((String) in.readValue((String.class.getClassLoader())));
        this.pickUpServiceAvailable = ((String) in.readValue((String.class.getClassLoader())));
        this.branchTypeIdentifier = ((String) in.readValue((String.class.getClassLoader())));

    }

    public DealerMasterResponse() {
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDealerSource() {
        return dealerSource;
    }

    public void setDealerSource(String dealerSource) {
        this.dealerSource = dealerSource;
    }

    public String getLatlng() {
        return latitude + "," + longitude;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getAddress() {
        if (addressLine1 != null && !addressLine1.isEmpty() && addressLine2 != null && !addressLine2.isEmpty()){
            return addressLine1 + "," + addressLine2;
        } else if (addressLine1 != null && !addressLine1.isEmpty()) {
            return addressLine1;
        } else if (addressLine2 != null && !addressLine2.isEmpty()) {
            return addressLine2;
        } else {
            return "";
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsService() {
        return isService;
    }

    public void setIsService(Boolean isService) {
        this.isService = isService;
    }

    public Boolean getPickupandDropAvailable() {
        return isPickupandDropAvailable;
    }

    public void setPickupandDropAvailable(Boolean pickupandDropAvailable) {
        isPickupandDropAvailable = pickupandDropAvailable;
    }

    public Boolean getDoorStepServiceAvailableBoolean() {
        return isDoorStepServiceAvailable;
    }

    public void setDoorStepServiceAvailableBoolean(Boolean doorStepServiceAvailable) {
        isDoorStepServiceAvailable = doorStepServiceAvailable;
    }
    public String getDoorStepServiceAvailable() {
        return doorStepServiceAvailable;
    }

    public void setDoorStepServiceAvailable(String doorStepServiceAvailable) {
        this.doorStepServiceAvailable = doorStepServiceAvailable;
    }

    public String getPickUpServiceAvailable() {
        return pickUpServiceAvailable;
    }

    public void setPickUpServiceAvailable(String pickUpServiceAvailable) {
        this.pickUpServiceAvailable = pickUpServiceAvailable;
    }

    public String getBranchTypeIdentifier() {
        return branchTypeIdentifier;
    }

    public void setBranchTypeIdentifier(String branchTypeIdentifier) {
        this.branchTypeIdentifier = branchTypeIdentifier;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(dealerId);
        dest.writeValue(state);
        dest.writeValue(pincode);
        dest.writeValue(phone);
        dest.writeValue(longitude);
        dest.writeValue(latitude);
        dest.writeValue(emailId);
        dest.writeValue(dealerName);
        dest.writeValue(city);
        dest.writeValue(branchName);
        dest.writeValue(addressLine2);
        dest.writeValue(addressLine1);
        dest.writeValue(address);
        dest.writeValue(branchCode);
        dest.writeValue(distance);
        dest.writeValue(dealerSource);
        dest.writeValue(isService);
        dest.writeValue(latlng);
        dest.writeValue(doorStepServiceAvailable);
        dest.writeValue(pickUpServiceAvailable);
        dest.writeValue(branchTypeIdentifier);
    }

    public int describeContents() {
        return 0;
    }
}
