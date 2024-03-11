package com.royalenfield.reprime.models.response.web.createride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("moderationStatus")
    @Expose
    private String moderationStatus;
    @SerializedName("geo")
    @Expose
    private Geo geo;
    @SerializedName("pageStatus")
    @Expose
    private String pageStatus;
    @SerializedName("rideType")
    @Expose
    private String rideType;
    @SerializedName("rideStatus")
    @Expose
    private String rideStatus;
    @SerializedName("rideCategory")
    @Expose
    private List<String> rideCategory = null;
    @SerializedName("rideImages")
    @Expose
    private List<RideImage> rideImages = null;
    @SerializedName("rideInfo")
    @Expose
    private RideInfo rideInfo;
    @SerializedName("invitedMembers")
    @Expose
    private List<Object> invitedMembers = null;
    @SerializedName("ridersJoined")
    @Expose
    private List<Object> ridersJoined = null;
    @SerializedName("waypoints")
    @Expose
    private List<Object> waypoints = null;
    @SerializedName("endPointCoordinates")
    @Expose
    private List<String> endPointCoordinates = null;
    @SerializedName("endPoint")
    @Expose
    private String endPoint;
    @SerializedName("startPointCoordinates")
    @Expose
    private List<String> startPointCoordinates = null;
    @SerializedName("startPoint")
    @Expose
    private String startPoint;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("ridePageUrl")
    @Expose
    private Object ridePageUrl;
    @SerializedName("rideName")
    @Expose
    private String rideName;
    @SerializedName("locality")
    @Expose
    private Locality locality;

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public String getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(String pageStatus) {
        this.pageStatus = pageStatus;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
    }

    public List<String> getRideCategory() {
        return rideCategory;
    }

    public void setRideCategory(List<String> rideCategory) {
        this.rideCategory = rideCategory;
    }

    public List<RideImage> getRideImages() {
        return rideImages;
    }

    public void setRideImages(List<RideImage> rideImages) {
        this.rideImages = rideImages;
    }

    public RideInfo getRideInfo() {
        return rideInfo;
    }

    public void setRideInfo(RideInfo rideInfo) {
        this.rideInfo = rideInfo;
    }

    public List<Object> getInvitedMembers() {
        return invitedMembers;
    }

    public void setInvitedMembers(List<Object> invitedMembers) {
        this.invitedMembers = invitedMembers;
    }

    public List<Object> getRidersJoined() {
        return ridersJoined;
    }

    public void setRidersJoined(List<Object> ridersJoined) {
        this.ridersJoined = ridersJoined;
    }

    public List<Object> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Object> waypoints) {
        this.waypoints = waypoints;
    }

    public List<String> getEndPointCoordinates() {
        return endPointCoordinates;
    }

    public void setEndPointCoordinates(List<String> endPointCoordinates) {
        this.endPointCoordinates = endPointCoordinates;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public List<String> getStartPointCoordinates() {
        return startPointCoordinates;
    }

    public void setStartPointCoordinates(List<String> startPointCoordinates) {
        this.startPointCoordinates = startPointCoordinates;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Object getRidePageUrl() {
        return ridePageUrl;
    }

    public void setRidePageUrl(Object ridePageUrl) {
        this.ridePageUrl = ridePageUrl;
    }

    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

}