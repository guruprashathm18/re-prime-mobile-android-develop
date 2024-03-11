package com.royalenfield.reprime.models.request.web.rides;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CreateRideRequest implements Serializable {

    @SerializedName("startPoint")
    @Expose
    private String startPoint;
    @SerializedName("startPointCoordinates")
    @Expose
    private ArrayList<Double> startPointCoordinates = null;
    @SerializedName("endPoint")
    @Expose
    private String endPoint;
    @SerializedName("endPointCoordinates")
    @Expose
    private ArrayList<Double> endPointCoordinates = null;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;

    /*@SerializedName("createdOn")
    @Expose
    private String createdOn;
*/
    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("rideId")
    @Expose
    private String rideId;

    @SerializedName("rideName")
    @Expose
    private String rideName;
    @SerializedName("waypoints")
    @Expose
    private ArrayList<WayPointsData> waypoints = null;
    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("durationInDays")
    @Expose
    private String durationInDays;
    @SerializedName("totalDistance")
    @Expose
    private String totalDistance;
    @SerializedName("rideDetails")
    @Expose
    private String rideDetails;
    @SerializedName("terrainType")
    @Expose
    private String terrainType;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("rideStatus")
    @Expose
    private String rideStatus;
    @SerializedName("rideCategory")
    @Expose
    private String rideCategory = null;
    @SerializedName("rideType")
    @Expose
    private String rideType;
    @SerializedName("hashTags")
    @Expose
    private List<String> hashTags;

    /*@SerializedName("rideImages")
    @Expose
    private List<File> rideImages = null;
*/
    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public ArrayList<Double> getStartPointCoordinates() {
        return startPointCoordinates;
    }

    public void setStartPointCoordinates(ArrayList<Double> startPointCoordinates) {
        this.startPointCoordinates = startPointCoordinates;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public ArrayList<Double> getEndPointCoordinates() {
        return endPointCoordinates;
    }

    public void setEndPointCoordinates(ArrayList<Double> endPointCoordinates) {
        this.endPointCoordinates = endPointCoordinates;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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

    public String getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(String durationInDays) {
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

    public String getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
    }

    public String getRideCategory() {
        return rideCategory;
    }

    public void setRideCategory(String rideCategory) {
        this.rideCategory = rideCategory;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public ArrayList<WayPointsData> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<WayPointsData> waypoints) {
        this.waypoints = waypoints;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

/*
    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
*/

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

  /*  public List<File> getRideImages() {
        return rideImages;
    }

    public void setRideImages(List<File> rideImages) {
        this.rideImages = rideImages;
    }*/
}
