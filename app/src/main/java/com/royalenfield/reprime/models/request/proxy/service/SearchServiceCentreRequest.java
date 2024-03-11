package com.royalenfield.reprime.models.request.proxy.service;

public class SearchServiceCentreRequest {

    private String latitude;
    private String longitude;
    private int distance;
    private String searchtext;
    private boolean includeAllDealers;
    private boolean  isTextSearch;

    public SearchServiceCentreRequest(String lat, String lng, int distance, String searchtext, boolean includeAllDealers, boolean isTextSearch) {
        this.latitude = lat;
        this.longitude = lng;
        this.distance = distance;
        this.searchtext = searchtext;
        this.includeAllDealers = includeAllDealers;
        this.isTextSearch = isTextSearch;
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

    public String getSearchtext() {
        return searchtext;
    }

    public void setSearchtext(String searchtext) {
        this.searchtext = searchtext;
    }

    public boolean isIncludeAllDealers() {
        return includeAllDealers;
    }

    public void setIncludeAllDealers(boolean includeAllDealers) {
        this.includeAllDealers = includeAllDealers;
    }

    public boolean isTextSearch() {
        return isTextSearch;
    }

    public void setTextSearch(boolean textSearch) {
        isTextSearch = textSearch;
    }

}
