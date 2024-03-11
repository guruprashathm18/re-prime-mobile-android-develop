package com.royalenfield.reprime.models.response.firestore.ourworld;

import java.util.ArrayList;
import java.util.Map;

public class EventsResponse {

    private String _id;

    private String description;

    private String destination;

    private String duration;

    private String endDate;

    private ArrayList<Map<String, String>> gallery;

    private ArrayList<Map<String, Object>> itinerary;

    private String lightWeightPageUrl;

    private ArrayList<Map<String, String>> policyUrls;

    private ArrayList<Map<String, String>> price;

    private String ridersLimit;

    private String startDate;

    private String thumbnailImageSrc;

    private String title;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getLightWeightPageUrl() {
        return lightWeightPageUrl;
    }

    public void setLightWeightPageUrl(String lightWeightPageUrl) {
        this.lightWeightPageUrl = lightWeightPageUrl;
    }

    public ArrayList<Map<String, String>> getPolicyUrls() {
        return policyUrls;
    }

    public void setPolicyUrls(ArrayList<Map<String, String>> policyUrls) {
        this.policyUrls = policyUrls;
    }

    public ArrayList<Map<String, String>> getPrice() {
        return price;
    }

    public void setPrice(ArrayList<Map<String, String>> price) {
        this.price = price;
    }

    public String getRidersLimit() {
        return ridersLimit;
    }

    public void setRidersLimit(String ridersLimit) {
        this.ridersLimit = ridersLimit;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getThumbnailImageSrc() {
        return thumbnailImageSrc;
    }

    public void setThumbnailImageSrc(String thumbnailImageSrc) {
        this.thumbnailImageSrc = thumbnailImageSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
