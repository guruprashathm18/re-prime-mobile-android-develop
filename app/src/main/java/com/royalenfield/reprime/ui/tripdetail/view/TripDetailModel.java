package com.royalenfield.reprime.ui.tripdetail.view;

public class TripDetailModel {

    private String  mTripId;
    private String mSource;
    private String mDestination;
    private String mDistance;
    private String mTripStartTime;
    private String mTimeInHr;
    private String mTimeInMin;

    public TripDetailModel(String id, String mSource
            , String mDestination, String mDistance
            , String mTripStartTime, String mTimeInHr, String mTimeInMin) {

        this.mTripId = id;
        this.mSource = mSource;
        this.mDestination = mDestination;
        this.mDistance = mDistance;
        this.mTripStartTime = mTripStartTime;
        this.mTimeInHr = mTimeInHr;
        this.mTimeInMin = mTimeInMin;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public void setDestination(String mDestination) {
        this.mDestination = mDestination;
    }

    public void setDistance(String mDistance) {
        this.mDistance = mDistance;
    }

    public void setTripStartTime(String mTripStartTime) {
        this.mTripStartTime = mTripStartTime;
    }

    public String getSource() {
        return mSource;
    }

    public String getDestination() {
        return mDestination;
    }

    public String getTripId() {
        return mTripId;
    }

    public String getDistance() {
        return mDistance;
    }

    public String getTripStartTime() {
        return mTripStartTime;
    }

    public String getTimeInHr() {
        return mTimeInHr;
    }

    public void setTimeInHr(String mTimeInHr) {
        this.mTimeInHr = mTimeInHr;
    }

    public String getmTimeInMin() {
        return mTimeInMin;
    }

    public void setmTimeInMin(String mTimeInMin) {
        this.mTimeInMin = mTimeInMin;
    }
}
