package com.royalenfield.reprime.ui.home.rides.listeners;

import com.google.android.gms.maps.model.LatLng;

/**
 * This interface is to update the location in Create & PlanRide screens
 */
public interface CurrentLocationListener {

    void updateCurrentLocation(LatLng latLng);
}
