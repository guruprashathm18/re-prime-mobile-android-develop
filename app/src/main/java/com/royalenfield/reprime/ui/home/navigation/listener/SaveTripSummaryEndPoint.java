package com.royalenfield.reprime.ui.home.navigation.listener;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveTripSummaryEndPoint {
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    /**
     * No args constructor for use in serialization
     *
     */
    public SaveTripSummaryEndPoint() {
    }

    /**
     *
     * @param latitude
     * @param longitude
     */
    public SaveTripSummaryEndPoint(Double latitude, Double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "SaveTripSummaryEndPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}