package com.royalenfield.reprime.models.response.firestore.rides;

import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.RidersJoined;
import com.royalenfield.reprime.models.response.web.createride.RideInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpcomingRidesResponse {

    private String _id;

    private String createdBy;

    private String createdOn;

    private String endPoint;

    private ArrayList<Map<String, String>> rideImages;

    private RideInfo rideInfo;

    private String rideName;

    private String ridePageUrl;

    private String rideType;

    private ArrayList<RidersJoined> ridersJoined;

    private String startPoint;

    private ArrayList<Double> startPointCoordinates;

    private Map<String, Object> userInfo;

    private List<WayPointsData> waypoints;

    private float distance;

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public ArrayList<Map<String, String>> getRideImages() {
        return rideImages;
    }

    public void setRideImages(ArrayList<Map<String, String>> rideImages) {
        this.rideImages = rideImages;
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

    public String getRidePageUrl() {
        return ridePageUrl;
    }

    public void setRidePageUrl(String ridePageUrl) {
        this.ridePageUrl = ridePageUrl;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public ArrayList<RidersJoined> getRidersJoined() {
        return ridersJoined;
    }

    public void setRidersJoined(ArrayList<RidersJoined> ridersJoined) {
        this.ridersJoined = ridersJoined;
    }

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

    public Map<String, Object> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String, Object> userInfo) {
        this.userInfo = userInfo;
    }

    public List<WayPointsData> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<WayPointsData> waypoints) {
        this.waypoints = waypoints;
    }

}
