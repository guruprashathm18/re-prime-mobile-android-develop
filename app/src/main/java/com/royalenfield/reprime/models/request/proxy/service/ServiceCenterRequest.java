package com.royalenfield.reprime.models.request.proxy.service;

public class ServiceCenterRequest {

    private String latitude;

    private String longitude;

    private int distance;

    public ServiceCenterRequest(String lat, String lng, int distance) {
        this.latitude = lat;
        this.longitude = lng;
        this.distance = distance;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
