package com.royalenfield.reprime.models.request.web.joinride;

public class JoinRideRequest {

    private String rideType;
    private String rideId;

    public JoinRideRequest(String rideType, String rideId) {
        this.rideType = rideType;
        this.rideId = rideId;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }
}
