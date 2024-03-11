package com.royalenfield.reprime.models.request.web.rides;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCheckInRequest {

    @SerializedName("checkInPlaceName")
    @Expose
    private String checkInPlaceName;
    @SerializedName("checkInDescription")
    @Expose
    private String checkInDescription;
    @SerializedName("checkInCategory")
    @Expose
    private String checkInCategory;
    @SerializedName("checkInImage")
    @Expose
    private String checkInImage = null;
    @SerializedName("rideId")
    @Expose
    private String rideId;
    @SerializedName("address")
    @Expose
    private String address;

    public String getCheckInPlaceName() {
        return checkInPlaceName;
    }

    public void setCheckInPlaceName(String checkInPlaceName) {
        this.checkInPlaceName = checkInPlaceName;
    }

    public String getCheckInDescription() {
        return checkInDescription;
    }

    public void setCheckInDescription(String checkInDescription) {
        this.checkInDescription = checkInDescription;
    }

    public String getCheckInCategory() {
        return checkInCategory;
    }

    public void setCheckInCategory(String checkInCategory) {
        this.checkInCategory = checkInCategory;
    }

    public String getCheckInImage() {
        return checkInImage;
    }

    public void setCheckInImage(String checkInImage) {
        this.checkInImage = checkInImage;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}