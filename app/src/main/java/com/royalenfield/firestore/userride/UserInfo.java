package com.royalenfield.firestore.userride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfo {

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profileURL")
    @Expose
    private String profileURL;
    @SerializedName("ridesCreatedCount")
    @Expose
    private String ridesCreatedCount;
    @SerializedName("ridesJoined")
    @Expose
    private List<Object> ridesJoined = null;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getRidesCreatedCount() {
        return ridesCreatedCount;
    }

    public void setRidesCreatedCount(String ridesCreatedCount) {
        this.ridesCreatedCount = ridesCreatedCount;
    }

    public List<Object> getRidesJoined() {
        return ridesJoined;
    }

    public void setRidesJoined(List<Object> ridesJoined) {
        this.ridesJoined = ridesJoined;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

}