package com.royalenfield.reprime.models.response.web.createride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideInfo {

    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("durationInDays")
    @Expose
    private int durationInDays;
    @SerializedName("totalDistance")
    @Expose
    private String totalDistance;
    @SerializedName("rideDetails")
    @Expose
    private String rideDetails;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("terrainType")
    @Expose
    private String terrainType;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("rideStartDateIso")
    @Expose
    private String rideStartDateIso;
    @SerializedName("rideEndDateIso")
    @Expose
    private String rideEndDateIso;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getRideDetails() {
        return rideDetails;
    }

    public void setRideDetails(String rideDetails) {
        this.rideDetails = rideDetails;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(String terrainType) {
        this.terrainType = terrainType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRideStartDateIso() {
        return rideStartDateIso;
    }

    public void setRideStartDateIso(String rideStartDateIso) {
        this.rideStartDateIso = rideStartDateIso;
    }

    public String getRideEndDateIso() {
        return rideEndDateIso;
    }

    public void setRideEndDateIso(String rideEndDateIso) {
        this.rideEndDateIso = rideEndDateIso;
    }

}