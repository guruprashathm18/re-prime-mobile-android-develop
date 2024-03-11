package com.royalenfield.reprime.ui.home.navigation.listener;


import android.location.Location;

public interface NavigationRouteListener {
    void setRouteDuration(Long longDuration);

    void setReconstructedRouteInfo(Location source, Location destination);

    void setRouteEngineFailed(String s);

    void setUpdatedRouteDistance(double distance);

    void setUpdatedRouteDuration(long duration);

    void showNavigationRerouteMessage(boolean isVisible);

    void showDebugMessage(String text);
}
