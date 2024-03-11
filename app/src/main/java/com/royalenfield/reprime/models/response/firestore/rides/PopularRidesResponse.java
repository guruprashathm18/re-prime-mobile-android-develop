package com.royalenfield.reprime.models.response.firestore.rides;

import java.util.ArrayList;
import java.util.Map;

/**
 * Response for Popular rides list which gets from firestore
 */
public class PopularRidesResponse {

    private String title;

    private String startDateString;

    private String endDateString;

    private String duration;

    private String thumbnailImagePath;

    private String destination;

    private String distance;

    private String terrain;

    private String iconLogoImagePath;

    private String lightWeightPageUrl;

    private String description;

    private ArrayList<Map<String, String>> price;

    private ArrayList<Map<String, Object>> waypoints;

    private ArrayList<Map<String, Object>> itinerary;

    private ArrayList<Map<String, String>> gallery;

    private ArrayList<Map<String, String>> policyUrls;

    private String startPointLatitude;

    private String startPointLongitude;

    private float distanceCalculated;

    public float getDistanceCalculated() {
        return distanceCalculated;
    }

    public void setDistanceCalculated(float distanceCalculated) {
        this.distanceCalculated = distanceCalculated;
    }

    public String getStartPointLatitude() {
        return startPointLatitude;
    }

    public void setStartPointLatitude(String startPointLatitude) {
        this.startPointLatitude = startPointLatitude;
    }

    public String getStartPointLongitude() {
        return startPointLongitude;
    }

    public void setStartPointLongitude(String startPointLongitude) {
        this.startPointLongitude = startPointLongitude;
    }

    public ArrayList<Map<String, String>> getPolicyUrls() {
        return policyUrls;
    }

    public void setPolicyUrls(ArrayList<Map<String, String>> policyUrls) {
        this.policyUrls = policyUrls;
    }

    public ArrayList<Map<String, String>> getGallery() {
        return gallery;
    }

    public void setGallery(ArrayList<Map<String, String>> gallery) {
        this.gallery = gallery;
    }

    public ArrayList<Map<String, Object>> getItinerary() {
        return itinerary;
    }

    public void setItinerary(ArrayList<Map<String, Object>> itinerary) {
        this.itinerary = itinerary;
    }

    private String rideType;

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public ArrayList<Map<String, Object>> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<Map<String, Object>> waypoints) {
        this.waypoints = waypoints;
    }

    public ArrayList<Map<String, String>> getPrice() {
        return price;
    }

    public void setPrice(ArrayList<Map<String, String>> price) {
        this.price = price;
    }

    public String getLightWeightPageUrl() {
        return lightWeightPageUrl;
    }

    public void setLightWeightPageUrl(String lightWeightPageUrl) {
        this.lightWeightPageUrl = lightWeightPageUrl;
    }

    public String getIconLogoImagePath() {
        return iconLogoImagePath;
    }

    public void setIconLogoImagePath(String iconLogoImagePath) {
        this.iconLogoImagePath = iconLogoImagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getThumbnailImagePath() {
        return thumbnailImagePath;
    }

    public void setThumbnailImagePath(String thumbnailImagePath) {
        this.thumbnailImagePath = thumbnailImagePath;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
