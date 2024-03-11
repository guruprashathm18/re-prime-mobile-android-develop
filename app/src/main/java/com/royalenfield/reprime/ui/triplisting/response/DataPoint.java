package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPoint {

	@SerializedName("ts")
	@Expose
	private String ts;
	@SerializedName("lat")
	@Expose
	private Double lat;
	@SerializedName("lng")
	@Expose
	private Double lng;
	@SerializedName("speed")
	@Expose
	private Integer speed;
	@SerializedName("isHA")
	@Expose
	private Boolean isHA;
	@SerializedName("isHB")
	@Expose
	private Boolean isHB;
	@SerializedName("address")
	@Expose
	private String address;

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Boolean getIsHA() {
		return isHA;
	}

	public void setIsHA(Boolean isHA) {
		this.isHA = isHA;
	}

	public Boolean getIsHB() {
		return isHB;
	}

	public void setIsHB(Boolean isHB) {
		this.isHB = isHB;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}