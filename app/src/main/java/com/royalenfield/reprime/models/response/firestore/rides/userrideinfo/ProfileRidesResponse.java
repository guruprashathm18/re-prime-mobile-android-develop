package com.royalenfield.reprime.models.response.firestore.rides.userrideinfo;

import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.web.createride.RideImage;
import com.royalenfield.reprime.models.response.web.createride.RideInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileRidesResponse {

    private String _id;

    private String endPoint;

    private String startPoint;

    private String createdBy;

    private String createdOn;

    private String ridePageUrl;

    private ArrayList<Double> endPointCoordinates;

    private ArrayList<RideImage> rideImages;

    private ArrayList<Double> startPointCoordinates;

    private ArrayList<String> rideCategory;

    private String moderationStatus;

    private RideInfo rideInfo;

    private String rideName = "";

    private String rideType = "";

    private List<WayPointsData> waypoints = null;

    private ArrayList<RidersJoined> ridersJoined;

    private Map<String , Object> userInfo;

    private String dealerName;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Map<String, Object> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String, Object> userInfo) {
        this.userInfo = userInfo;
    }

    private String pageStatus;

    public String getRidePageUrl() {
        return ridePageUrl;
    }

    public void setRidePageUrl(String ridePageUrl) {
        this.ridePageUrl = ridePageUrl;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public ArrayList<Double> getEndPointCoordinates() {
        return endPointCoordinates;
    }

    public void setEndPointCoordinates(ArrayList<Double> endPointCoordinates) {
        this.endPointCoordinates = endPointCoordinates;
    }

    public ArrayList<RideImage> getRideImages() {
        return rideImages;
    }

    public void setRideImages(ArrayList<RideImage> rideImages) {
        this.rideImages = rideImages;
    }

    public ArrayList<Double> getStartPointCoordinates() {
        return startPointCoordinates;
    }

    public void setStartPointCoordinates(ArrayList<Double> startPointCoordinates) {
        this.startPointCoordinates = startPointCoordinates;
    }

    public RideInfo getRideInfo() {
        return rideInfo;
    }

    public void setRideInfo(RideInfo rideInfo) {
        this.rideInfo = rideInfo;
    }

    public List<WayPointsData> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<WayPointsData> waypoints) {
        this.waypoints = waypoints;
    }

    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public ArrayList<RidersJoined> getRidersJoined() {
        return ridersJoined;
    }


    public void setRidersJoined(ArrayList<RidersJoined> ridersJoined) {
        this.ridersJoined = ridersJoined;
    }

    public ArrayList<String> getRideCategory() {
        return rideCategory;
    }

    public void setRideCategory(ArrayList<String> rideCategory) {
        this.rideCategory = rideCategory;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
