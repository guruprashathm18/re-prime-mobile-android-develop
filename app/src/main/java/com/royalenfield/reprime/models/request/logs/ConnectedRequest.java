package com.royalenfield.reprime.models.request.logs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectedRequest {

	@SerializedName("guid")
	@Expose
	private String guid;
	@SerializedName("vin")
	@Expose
	private String vin;
	@SerializedName("imei")
	@Expose
	private String imei;
	@SerializedName("from")
	@Expose
	private String from;
	@SerializedName("to")
	@Expose
	private String to;
	@SerializedName("alertTimeStamp")
	@Expose
	private String alertTimeStamp;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getAlertTimeStamp() {
		return alertTimeStamp;
	}

	public void setAlertTimeStamp(String alertTimeStamp) {
		this.alertTimeStamp = alertTimeStamp;
	}
}
