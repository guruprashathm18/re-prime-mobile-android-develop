package com.royalenfield.firestore.rides.userrideinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;

public class DealerInfo {

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("dealer_image")
    @Expose
    private String dealer_image;
    @SerializedName("coordinates")
    @Expose
    private WayPointsData coordinates;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phoneNo;
    }

    public void setPhone(String phone) {
        this.phoneNo = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDealer_image() {
        return dealer_image;
    }

    public void setDealer_image(String dealer_image) {
        this.dealer_image = dealer_image;
    }

    public WayPointsData getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(WayPointsData coordinates) {
        this.coordinates = coordinates;
    }
}
