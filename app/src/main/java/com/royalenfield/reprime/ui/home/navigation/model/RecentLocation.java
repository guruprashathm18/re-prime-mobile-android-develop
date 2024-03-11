package com.royalenfield.reprime.ui.home.navigation.model;

import com.google.android.gms.maps.model.LatLng;

public class RecentLocation {
    String locationName;
    String locationFormattedAddress;
    LatLng locationCoordinates;

    public RecentLocation(String locationName,String locationFormattedAddress,LatLng locationCoordinates){
        this.locationName = locationName;
        this.locationFormattedAddress = locationFormattedAddress;
        this.locationCoordinates = locationCoordinates;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationFormattedAddress() {
        return locationFormattedAddress;
    }

    public void setLocationFormattedAddress(String locationFormattedAddress) {
        this.locationFormattedAddress = locationFormattedAddress;
    }

    public LatLng getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(LatLng locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

}
