package com.royalenfield.firestore.userride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.web.createride.Locality;
import com.royalenfield.reprime.models.response.web.createride.RideImage;
import com.royalenfield.reprime.models.response.web.createride.RideInfo;

import java.util.List;

public class Data {

    @SerializedName("locality")
    @Expose
    private Locality locality;
    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("rideInfo")
    @Expose
    private RideInfo rideInfo;
    @SerializedName("rideName")
    @Expose
    private String rideName;
    @SerializedName("ridePageUrl")
    @Expose
    private Object ridePageUrl;
    @SerializedName("startPoint")
    @Expose
    private String startPoint;
    @SerializedName("startPointCoordinates")
    @Expose
    private List<Double> startPointCoordinates = null;
    @SerializedName("endPoint")
    @Expose
    private String endPoint;
    @SerializedName("endPointCoordinates")
    @Expose
    private List<Double> endPointCoordinates = null;
    @SerializedName("ridersJoined")
    @Expose
    private List<Object> ridersJoined = null;
    @SerializedName("invitedMembers")
    @Expose
    private List<Object> invitedMembers = null;
    @SerializedName("rideCategory")
    @Expose
    private List<String> rideCategory = null;
    @SerializedName("rideStatus")
    @Expose
    private String rideStatus;
    @SerializedName("rideType")
    @Expose
    private String rideType;
    @SerializedName("pageStatus")
    @Expose
    private String pageStatus;
    @SerializedName("hashTags")
    @Expose
    private List<Object> hashTags = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("rideImages")
    @Expose
    private List<RideImage> rideImages = null;
    @SerializedName("waypoints")
    @Expose
    private List<WayPointsData> waypoints = null;
    @SerializedName("lastModified")
    @Expose
    private String lastModified;

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public RideInfo getRideInfo() {
        return rideInfo;
    }

    public void setRideInfo(RideInfo rideInfo) {
        this.rideInfo = rideInfo;
    }

    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public Object getRidePageUrl() {
        return ridePageUrl;
    }

    public void setRidePageUrl(Object ridePageUrl) {
        this.ridePageUrl = ridePageUrl;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public List<Double> getStartPointCoordinates() {
        return startPointCoordinates;
    }

    public void setStartPointCoordinates(List<Double> startPointCoordinates) {
        this.startPointCoordinates = startPointCoordinates;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public List<Double> getEndPointCoordinates() {
        return endPointCoordinates;
    }

    public void setEndPointCoordinates(List<Double> endPointCoordinates) {
        this.endPointCoordinates = endPointCoordinates;
    }

    public List<Object> getRidersJoined() {
        return ridersJoined;
    }

    public void setRidersJoined(List<Object> ridersJoined) {
        this.ridersJoined = ridersJoined;
    }

    public List<Object> getInvitedMembers() {
        return invitedMembers;
    }

    public void setInvitedMembers(List<Object> invitedMembers) {
        this.invitedMembers = invitedMembers;
    }

    public List<String> getRideCategory() {
        return rideCategory;
    }

    public void setRideCategory(List<String> rideCategory) {
        this.rideCategory = rideCategory;
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public String getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(String pageStatus) {
        this.pageStatus = pageStatus;
    }

    public List<Object> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<Object> hashTags) {
        this.hashTags = hashTags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public List<RideImage> getRideImages() {
        return rideImages;
    }

    public void setRideImages(List<RideImage> rideImages) {
        this.rideImages = rideImages;
    }

    public List<WayPointsData> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<WayPointsData> waypoints) {
        this.waypoints = waypoints;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}