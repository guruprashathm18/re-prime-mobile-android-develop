package com.royalenfield.reprime.models.response.firestore.rides;

import java.util.ArrayList;
import java.util.Map;

/**
 * Response class for DealerUpcomingRides fetched from Firestore
 */
public class DealerUpcomingRidesResponse {


    private String _id;

    private Map<String, Map<String, Object>> dealerInfo;

    private String dealerName;

    private String endPoint;

    private ArrayList<Map<String, String>> rideImages;

    private Map<String, Object> rideInfo;

    private String rideName;

    private ArrayList<Map<String , Object>> ridersJoined;

    private String startPoint;

    private ArrayList<Long> startPointCoordinates;

    private float distance;

    public DealerUpcomingRidesResponse(String _id, Map<String, Map<String, Object>> dealerInfo, String  dealerName, String endPoint,
                                       ArrayList<Map<String, String>> rideImages, Map<String, Object> rideInfo, String rideName,
                                       ArrayList<Map<String , Object>> ridersJoined, String startPoint, ArrayList<Long> startPointCoordinates) {
        this._id = _id;
        this.dealerInfo = dealerInfo;
        this.dealerName = dealerName;
        this.endPoint = endPoint;
        this.rideImages = rideImages;
        this.rideInfo = rideInfo;
        this.rideName = rideName;
        this.ridersJoined = ridersJoined;
        this.startPoint = startPoint;
        this.startPointCoordinates = startPointCoordinates;

    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String get_id() {
        return _id;
    }

    public ArrayList<Long> getStartPointCoordinates() {
        return startPointCoordinates;
    }

    public void setStartPointCoordinates(ArrayList<Long> startPointCoordinates) {
        this.startPointCoordinates = startPointCoordinates;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Map<String, Map<String, Object>> getDealerRideInfo() {
        return dealerInfo;
    }

    public void setDealerRideInfo(Map<String, Map<String, Object>> dealerInfo) {
        this.dealerInfo = dealerInfo;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
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

    public Map<String, Object> getRideInfo() {
        return rideInfo;
    }

    public void setRideInfo(Map<String, Object> rideInfo) {
        this.rideInfo = rideInfo;
    }

    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public ArrayList<Map<String , Object>> getRidersJoined() {
        return ridersJoined;
    }

    public void setRidersJoined(ArrayList<Map<String , Object>> ridersJoined) {
        this.ridersJoined = ridersJoined;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

}
