package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RidesKeys {
    @SerializedName("eventFeatureImageURL")
    @Expose
    private String eventFeatureImageURL_disabled;
    @SerializedName("rideFeatureImageURL")
    @Expose
    private String rideFeatureImageURL_disabled;
    @SerializedName("geo_query_radius")
    @Expose
    private String geoquery_radius;
    @SerializedName("create_ride_message")
    @Expose
    private String message_ride_disabled;

    public String getEventFeatureImageURL_disabled() {
        return eventFeatureImageURL_disabled;
    }

    public void setEventFeatureImageURL_disabled(String eventFeatureImageURL_disabled) {
        this.eventFeatureImageURL_disabled = eventFeatureImageURL_disabled;
    }

    public String getRideFeatureImageURL_disabled() {
        return rideFeatureImageURL_disabled;
    }

    public void setRideFeatureImageURL_disabled(String rideFeatureImageURL_disabled) {
        this.rideFeatureImageURL_disabled = rideFeatureImageURL_disabled;
    }

    public String getGeoquery_radius() {
        return geoquery_radius;
    }

    public void setGeoquery_radius(String geoquery_radius) {
        this.geoquery_radius = geoquery_radius;
    }

    public String getMessage_ride_disabled() {
        return message_ride_disabled;
    }

    public void setMessage_ride_disabled(String message_ride_disabled) {
        this.message_ride_disabled = message_ride_disabled;
    }
}
