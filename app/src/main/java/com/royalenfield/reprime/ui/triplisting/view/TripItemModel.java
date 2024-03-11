package com.royalenfield.reprime.ui.triplisting.view;

import java.io.Serializable;

public class TripItemModel implements Serializable {

    private final String mStartloc;
    private final String mEndLoc;
    private final Integer mTotalHaCount;
    private final Integer mTotalHbCount;
    private final Integer mTotalIdlingTime;
    private String mTripId;
    private String mSource;
    private String mDestination;
    private String mDistance;
    private String mTripStartTime;
    private String mUnformattedStartTime;
    private int mOverSpeedingCount;
    private String mAverageSpeedVal;
    private String mTopSpeed;
    private String mTripEndTime;
    private double totalScore;
    private String fromTime;
    private String toTime;
	private boolean isChecked;
	private String tripType;
	private String tripCreatedTime;
private Long totalRunningTime;


    public TripItemModel(String id, String mSource
            , String mDestination, String mDistance
            , String mTripStartTime
            , String tripEndTime,String tripCraetedTime
            , String startloc, String endLoc
            , Integer totalHaCount
            , Integer totalHbCount, Integer totalIdlingTime,boolean isChecked,String tripType,Long totalRunningTime) {

		this.tripCreatedTime=tripCraetedTime;
        this.mTripId = id;
        this.mSource = mSource;
        this.mDestination = mDestination;
        this.mDistance = mDistance;
        this.mTripStartTime = mTripStartTime;
        this.mTripEndTime = tripEndTime;
        mStartloc = startloc;
        mEndLoc = endLoc;
        mTotalHaCount = totalHaCount;
        mTotalHbCount = totalHbCount;
        mTotalIdlingTime = totalIdlingTime;
		this.isChecked=isChecked;
		this.tripType=tripType;
		this.totalRunningTime=totalRunningTime;
    }

    public String getUnformattedStartTime() {
        return mUnformattedStartTime;
    }

    public void setUnformattedStartTime(String mUnformattedStartTime) {
        this.mUnformattedStartTime = mUnformattedStartTime;
    }

    public String getStartLoc() {
        return mStartloc;
    }

    public String getEndLoc() {
        return mEndLoc;
    }

    public Integer getmTotalHaCount() {
        return mTotalHaCount;
    }

    public Integer getmTotalHbCount() {
        return mTotalHbCount;
    }

    public Integer getmTotalIdlingTime() {
        return mTotalIdlingTime;
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

    public String getTripEndTime() {
        return mTripEndTime;
    }

    public void setOverSpeeding(int overSpeedCount) {
        mOverSpeedingCount = overSpeedCount;
    }

    public int getOverSpeedingCount() {
        return mOverSpeedingCount;
    }

    public String getAverageSpeedVal() {
        return mAverageSpeedVal;
    }

    public void setAverageSpeedVal(String mAverageSpeedVal) {
        this.mAverageSpeedVal = mAverageSpeedVal;
    }

    public String getmTopSpeed() {
        return mTopSpeed;
    }

    public void setmTopSpeed(String mTopSpeed) {
        this.mTopSpeed = mTopSpeed;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getTripCreatedTime() {
		return tripCreatedTime;
	}

	public Long getTotalRunningTime() {
		return totalRunningTime;
	}

	public void setTotalRunningTime(Long totalRunningTime) {
		this.totalRunningTime = totalRunningTime;
	}
}
