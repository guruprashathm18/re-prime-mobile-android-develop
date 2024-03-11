package com.royalenfield.reprime.models.response.web.dealerride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dealer {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("rideStartDateIso")
    @Expose
    private String rideStartDateIso;
    @SerializedName("rideEndDateIso")
    @Expose
    private String rideEndDateIso;
    @SerializedName("DealerId")
    @Expose
    private DealerId dealerId;
    @SerializedName("geo")
    @Expose
    private Geo geo;
    @SerializedName("personalInfo")
    @Expose
    private PersonalInfo personalInfo;
    @SerializedName("ridersCount")
    @Expose
    private Integer ridersCount;
    @SerializedName("rideCategory")
    @Expose
    private String rideCategory;
    @SerializedName("rideImages")
    @Expose
    private List<RideImage> rideImages = null;
    @SerializedName("totalDistance")
    @Expose
    private Integer totalDistance;
    @SerializedName("startTime")
    @Expose
    private Object startTime;
    @SerializedName("ridersJoined")
    @Expose
    private List<String> ridersJoined = null;
    @SerializedName("terrain")
    @Expose
    private String terrain;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("rideDetails")
    @Expose
    private Object rideDetails;
    @SerializedName("durationInDays")
    @Expose
    private Object durationInDays;
    @SerializedName("calculatedDistance")
    @Expose
    private Integer calculatedDistance;
    @SerializedName("durationInHours")
    @Expose
    private Object durationInHours;
    @SerializedName("waypoints")
    @Expose
    private List<Object> waypoints = null;
    @SerializedName("endPoint")
    @Expose
    private EndPoint endPoint;
    @SerializedName("startPoint")
    @Expose
    private StartPoint startPoint;
    @SerializedName("rideName")
    @Expose
    private String rideName;
    @SerializedName("createdOn")
    @Expose
    private Object createdOn;
    @SerializedName("ridePageUrl")
    @Expose
    private String ridePageUrl;
    @SerializedName("RideId")
    @Expose
    private Integer rideId;
    @SerializedName("prevUserId")
    @Expose
    private Object prevUserId;
    @SerializedName("difficulty")
    @Expose
    private Object difficulty;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("locality")
    @Expose
    private Locality locality;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
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

    public DealerId getDealerId() {
        return dealerId;
    }

    public void setDealerId(DealerId dealerId) {
        this.dealerId = dealerId;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Integer getRidersCount() {
        return ridersCount;
    }

    public void setRidersCount(Integer ridersCount) {
        this.ridersCount = ridersCount;
    }

    public String getRideCategory() {
        return rideCategory;
    }

    public void setRideCategory(String rideCategory) {
        this.rideCategory = rideCategory;
    }

    public List<RideImage> getRideImages() {
        return rideImages;
    }

    public void setRideImages(List<RideImage> rideImages) {
        this.rideImages = rideImages;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public List<String> getRidersJoined() {
        return ridersJoined;
    }

    public void setRidersJoined(List<String> ridersJoined) {
        this.ridersJoined = ridersJoined;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getRideDetails() {
        return rideDetails;
    }

    public void setRideDetails(Object rideDetails) {
        this.rideDetails = rideDetails;
    }

    public Object getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Object durationInDays) {
        this.durationInDays = durationInDays;
    }

    public Integer getCalculatedDistance() {
        return calculatedDistance;
    }

    public void setCalculatedDistance(Integer calculatedDistance) {
        this.calculatedDistance = calculatedDistance;
    }

    public Object getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(Object durationInHours) {
        this.durationInHours = durationInHours;
    }

    public List<Object> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Object> waypoints) {
        this.waypoints = waypoints;
    }

    public EndPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

    public StartPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(StartPoint startPoint) {
        this.startPoint = startPoint;
    }

    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public Object getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Object createdOn) {
        this.createdOn = createdOn;
    }

    public String getRidePageUrl() {
        return ridePageUrl;
    }

    public void setRidePageUrl(String ridePageUrl) {
        this.ridePageUrl = ridePageUrl;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public Object getPrevUserId() {
        return prevUserId;
    }

    public void setPrevUserId(Object prevUserId) {
        this.prevUserId = prevUserId;
    }

    public Object getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Object difficulty) {
        this.difficulty = difficulty;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

}
