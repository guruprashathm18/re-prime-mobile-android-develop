package com.royalenfield.reprime.models.response.web.checkin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("checkInPlaceName")
    @Expose
    private String checkInPlaceName;
    @SerializedName("checkInThumbnailImage")
    @Expose
    private String checkInThumbnailImage;
    @SerializedName("checkInCategory")
    @Expose
    private String checkInCategory;
    @SerializedName("popularity")
    @Expose
    private int popularity;
    @SerializedName("checkInDescription")
    @Expose
    private String checkInDescription;
    @SerializedName("address")
    @Expose
    private String address;

    public String getCheckInPlaceName() {
        return checkInPlaceName;
    }

    public void setCheckInPlaceName(String checkInPlaceName) {
        this.checkInPlaceName = checkInPlaceName;
    }

    public String getCheckInThumbnailImage() {
        return checkInThumbnailImage;
    }

    public void setCheckInThumbnailImage(String checkInThumbnailImage) {
        this.checkInThumbnailImage = checkInThumbnailImage;
    }

    public String getCheckInCategory() {
        return checkInCategory;
    }

    public void setCheckInCategory(String checkInCategory) {
        this.checkInCategory = checkInCategory;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getCheckInDescription() {
        return checkInDescription;
    }

    public void setCheckInDescription(String checkInDescription) {
        this.checkInDescription = checkInDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
