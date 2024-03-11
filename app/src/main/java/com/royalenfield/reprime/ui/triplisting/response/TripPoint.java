package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripPoint {

	@SerializedName("tripId")
	@Expose
	private String tripId;
	@SerializedName("points")
	@Expose
	private List<DataPoint> points;

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public List<DataPoint> getPoints() {
		return points;
	}

	public void setPoints(List<DataPoint> points) {
		this.points = points;
	}

}