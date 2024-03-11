package com.royalenfield.reprime.configuration;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigurationPrefManager {

    private static SharedPreferences configPref;
    private static Context _context;
    private static SharedPreferences.Editor editor;

    private static int PRIVATE_MODE = 0;

    private static ConfigurationPrefManager manager;


    private static final String KEY_CONFIG = "configuration";

    private static final int ROUTE_CALCULATION_MODE = 1;

    private static final String ROUTE_LIMIT = "ROUTE_LIMIT";
    private static final String SECONDARY_TBT_MIN_DISTANCE = "2ND_TBT_MIN_DISTANCE";
    private static final String OFF_ROUTE_TRIGGER_DISTANCE = "OFF_ROUTE_TRIGGER_DISTANCE";
    private static final String ETA_DISTANCE = "ETA_DISTANCE";
    private static final String ETA_OFF_TIME = "ETA_OFF_TIME";
    private static final String ENABLE_CALL_NOTIFICATION = "ENABLE_CALL_NOTIFICATION";


    private static final String USE_LIVE_TRAFFIC = "USE_LIVE_TRAFFIC";

    private static final String TRAFFIC_ENABLED = "TRAFFIC_ENABLED";
    private static final String BUILDINGS_ENABLED = "BUILDINGS_ENABLED";
    private static final String INDOOR_BUILDINGS_ENABLED = "INDOOR_BUILDINGS_ENABLED";
    private static final String COMPASS_ENABLED = "COMPASS_ENABLED";
    private static final String GESTURES_ROTATE_ENABLED = "GESTURES_ROTATE_ENABLED";
    private static final String GESTURES_ZOOM_ENABLED = "GESTURES_ZOOM_ENABLED";
    private static final String GESTURES_TILT_ENABLED = "GESTURES_TILT_ENABLED";
    private static final String NIGHT_MODE_ENABLED = "GESTURES_VIEW_ENABLED";
    private static final String CHANGE_ETA_ENABLED = "GESTURES_ETA_ENABLED";


    private static final String AVOID_FERRIES = "AVOID_FERRIES";
    private static final String AVOID_HIGHWAYS = "AVOID_HIGHWAYS";
    private static final String AVOID_TOLL_ROADS = "AVOID_TOLL_ROADS";

    private static final String CAR = "CAR";
    private static final String BICYCLE = "BICYCLE";
    private static final String PEDESTRIAN = "PEDESTRIAN";


    private static void init(Context context) {
        _context = context;
        configPref = _context.getSharedPreferences(KEY_CONFIG, PRIVATE_MODE);
        editor = configPref.edit();
    }

    public static ConfigurationPrefManager getInstance(Context _context) {
        if (manager == null) {
            manager = new ConfigurationPrefManager();
            init(_context);
        }
        return manager;
    }


    public void setLimit(int limit) {
        editor.putInt(ROUTE_LIMIT, limit);
        editor.commit();
    }

    public int getRouteLimit() {
        return configPref.getInt(ROUTE_LIMIT, 3);
    }

    public void setOFF_ROUTE_TriggerDistance(int dist) {
        editor.putInt(OFF_ROUTE_TRIGGER_DISTANCE, dist);
        editor.commit();
    }

    public int getOFF_ROUTE_TriggerDistance() {
        return configPref.getInt(OFF_ROUTE_TRIGGER_DISTANCE, 50);
    }

    public void setETA(boolean isETA) {
        editor.putBoolean(ETA_DISTANCE, isETA);
        editor.commit();
    }

    public Boolean getETA() {
        return configPref.getBoolean(ETA_DISTANCE, false);
    }

    public void setEnableCallNotification(boolean iscallEnabled) {
        editor.putBoolean(ENABLE_CALL_NOTIFICATION, iscallEnabled);
        editor.commit();
    }

    public Boolean getEnableCallNotification() {
        return configPref.getBoolean(ENABLE_CALL_NOTIFICATION, false);
    }
    public void setOffTime(long dist) {
        editor.putLong(ETA_OFF_TIME, dist);
        editor.commit();
    }

    public Long getOffTime() {
        return configPref.getLong(ETA_OFF_TIME, 20);
    }


    public void setMinDistance2ndTbt2RED(int dist) {
        editor.putInt(SECONDARY_TBT_MIN_DISTANCE, dist);
        editor.commit();
    }

    public int getMinDistance2ndTbt2RED() {
        return configPref.getInt(SECONDARY_TBT_MIN_DISTANCE, 60);
    }

    public void setPedestrian(boolean isPedestrian) {
        editor.putBoolean(PEDESTRIAN, isPedestrian);
        editor.commit();
    }

    public boolean isPedestrianEnabled() {
        return configPref.getBoolean(PEDESTRIAN, false);
    }

    public void setBicycle(boolean isBicycle) {
        editor.putBoolean(BICYCLE, isBicycle);
        editor.commit();
    }

    public boolean isBicycleEnabled() {
        return configPref.getBoolean(BICYCLE, false);
    }

    public void setCar(boolean isCar) {
        editor.putBoolean(CAR, isCar);
        editor.commit();
    }

    public boolean isCarEnabled() {
        return configPref.getBoolean(CAR, false);
    }

    public void setTollRoads(boolean isTollRoad) {
        editor.putBoolean(AVOID_TOLL_ROADS, isTollRoad);
        editor.commit();
    }

    public boolean isTollRoad() {
        return configPref.getBoolean(AVOID_TOLL_ROADS, false);
    }

    public void setHighways(boolean isHighway) {
        editor.putBoolean(AVOID_HIGHWAYS, isHighway);
        editor.commit();
    }

    public boolean isHighwaysEnabled() {
        return configPref.getBoolean(AVOID_HIGHWAYS, false);
    }

    public void setFerries(boolean isFerries) {
        editor.putBoolean(AVOID_FERRIES, isFerries);
        editor.commit();
    }

    public boolean isFerriesEnabled() {
        return configPref.getBoolean(AVOID_FERRIES, false);
    }


    public void setZoomEnabled(boolean isZoom) {
        editor.putBoolean(GESTURES_ZOOM_ENABLED, isZoom);
        editor.commit();
    }

    public boolean isZoomEnabled() {
        return configPref.getBoolean(GESTURES_ZOOM_ENABLED, true);
    }

    public void setRotateEnabled(boolean isRotate) {
        editor.putBoolean(GESTURES_ROTATE_ENABLED, isRotate);
        editor.commit();
    }

    public boolean isRotateEnabled() {
        return configPref.getBoolean(GESTURES_ROTATE_ENABLED, true);
    }

    public void setGestureTiltEnabled(boolean isTilt) {
        editor.putBoolean(GESTURES_TILT_ENABLED, isTilt);
        editor.commit();
    }

    public boolean isTiltEnabled() {
        return configPref.getBoolean(GESTURES_TILT_ENABLED, true);
    }


    public void setCompassEnabled(boolean isCompass) {
        editor.putBoolean(COMPASS_ENABLED, isCompass);
        editor.commit();
    }

    public boolean isCompassEnabled() {
        return configPref.getBoolean(COMPASS_ENABLED, true);
    }

    public void setIndoorBuildingEnabled(boolean isIndoorBuilding) {
        editor.putBoolean(INDOOR_BUILDINGS_ENABLED, isIndoorBuilding);
        editor.commit();
    }

    public boolean isIndoorBuildingEnabled() {
        return configPref.getBoolean(INDOOR_BUILDINGS_ENABLED, true);
    }

    public void setBuildingEnabled(boolean isBuilding) {
        editor.putBoolean(BUILDINGS_ENABLED, isBuilding);
        editor.commit();
    }

    public boolean isBuildingEnabled() {
        return configPref.getBoolean(BUILDINGS_ENABLED, true);
    }

    public void setTrafficEnabled(boolean isTraffic) {
        editor.putBoolean(TRAFFIC_ENABLED, isTraffic);
        editor.commit();
    }

    public boolean isTrafficEnabled() {
        return configPref.getBoolean(TRAFFIC_ENABLED, true);
    }

    public void setNightmodeEnabled(boolean isNightMode) {
        editor.putBoolean(NIGHT_MODE_ENABLED, isNightMode);
        editor.commit();
    }

    public boolean isNightModeEnabled() {
        return configPref.getBoolean(NIGHT_MODE_ENABLED, false);
    }

    public void setLiveTraffic(boolean isLive) {
        editor.putBoolean(USE_LIVE_TRAFFIC, isLive);
        editor.commit();
    }

    public boolean isLiveTraffic() {
        return configPref.getBoolean(USE_LIVE_TRAFFIC, true);
    }



}
