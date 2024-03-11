package com.royalenfield.reprime.ui.home.navigation.listener;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveTripSummaryStartPoint {

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
    public SaveTripSummaryStartPoint() {
    }

    /**
     *
     * @param latitude
     * @param longitude
     */
    public SaveTripSummaryStartPoint(Double latitude, Double longitude) {
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
        return "SaveTripSummaryStartPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
