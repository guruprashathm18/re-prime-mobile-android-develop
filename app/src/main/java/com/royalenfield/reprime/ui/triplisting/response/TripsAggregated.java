package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripsAggregated {




	@SerializedName("averageSpeed")
	@Expose
	private Double averageSpeed;
	@SerializedName("topSpeed")
	@Expose
	private Double topSpeed;
	@SerializedName("tripId")
	@Expose
	private String tripId;
	@SerializedName("fromTs")
	@Expose
	private String fromTs;

	@SerializedName("tripCreatedTs")
	@Expose
	private String tripCreatedTs="0";

	@SerializedName("toTs")
	@Expose
	private String toTs;
	@SerializedName("startLoc")
	@Expose
	private String startLoc;
	@SerializedName("endLoc")
	@Expose
	private String endLoc;
	@SerializedName("startAddress")
	@Expose
	private String startAddress;
	@SerializedName("endAddress")
	@Expose
	private String endAddress;
	@SerializedName("totalDist")
	@Expose
	private Double totalDist;
	@SerializedName("totalRunningTime")
	@Expose
	private Long totalRunningTime=0l;
	@SerializedName("totalHaCount")
	@Expose
	private Integer totalHaCount;
	@SerializedName("totalHbCount")
	@Expose
	private Integer totalHbCount;
	@SerializedName("overspeedCount")
	@Expose
	private Integer overspeedCount;
	@SerializedName("uniqueId")
	@Expose
	private String uniqueId;
	@SerializedName("totalIdlingTime")
	@Expose
	private Double totalIdlingTime;
	@SerializedName("vehicleNumber")
	@Expose
	private String vehicleNumber;
	@SerializedName("driverScore")
	@Expose
	private String driverScore;
	@SerializedName("tripType")
	@Expose
	private String tripType;


	public Double getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(Double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public Double getTopSpeed() {
		return topSpeed;
	}

	public void setTopSpeed(Double topSpeed) {
		this.topSpeed = topSpeed;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getFromTs() {
		return fromTs;
	}

	public void setFromTs(String fromTs) {
		this.fromTs = fromTs;
	}

	public String getToTs() {
		return toTs;
	}

	public void setToTs(String toTs) {
		this.toTs = toTs;
	}

	public String getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(String startLoc) {
		this.startLoc = startLoc;
	}

	public String getEndLoc() {
		return endLoc;
	}

	public void setEndLoc(String endLoc) {
		this.endLoc = endLoc;
	}

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public Double getTotalDist() {
		return totalDist;
	}

	public void setTotalDist(Double totalDist) {
		this.totalDist = totalDist;
	}

	public Long getTotalRunningTime() {
		return totalRunningTime;
	}

	public void setTotalRunningTime(Long totalRunningTime) {
		this.totalRunningTime = totalRunningTime;
	}

	public Integer getTotalHaCount() {
		return totalHaCount;
	}

	public void setTotalHaCount(Integer totalHaCount) {
		this.totalHaCount = totalHaCount;
	}

	public Integer getTotalHbCount() {
		return totalHbCount;
	}

	public void setTotalHbCount(Integer totalHbCount) {
		this.totalHbCount = totalHbCount;
	}

	public Integer getOverspeedCount() {
		return overspeedCount;
	}

	public void setOverspeedCount(Integer overspeedCount) {
		this.overspeedCount = overspeedCount;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Double getTotalIdlingTime() {
		return totalIdlingTime;
	}

	public void setTotalIdlingTime(Double totalIdlingTime) {
		this.totalIdlingTime = totalIdlingTime;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getDriverScore() {
		return driverScore;
	}

	public void setDriverScore(String driverScore) {
		this.driverScore = driverScore;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getTripCreatedTs() {
		return tripCreatedTs;
	}

	public void setTripCreatedTs(String tripCreatedTs) {
		this.tripCreatedTs = tripCreatedTs;
	}
}
