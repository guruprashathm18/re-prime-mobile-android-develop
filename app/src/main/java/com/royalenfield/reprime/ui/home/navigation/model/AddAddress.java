package com.royalenfield.reprime.ui.home.navigation.model;

import com.google.android.gms.maps.model.LatLng;

public class AddAddress {
    String locationName;
    LatLng locationCoordinates;
    int position;

    public AddAddress(String location_name, LatLng locationlatlng, int position){
        this.locationName = location_name;
        this.locationCoordinates = locationlatlng;
        this.position = position;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public LatLng getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(LatLng locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }
}
