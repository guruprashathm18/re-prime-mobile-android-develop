package com.royalenfield.reprime.ui.home.connected.motorcycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PairingMotorcycleRequestModel {
    @SerializedName("vin")
    @Expose
    private String vin;

    @SerializedName("token")
    @Expose
    private String token;

	@SerializedName("callingCode")
	@Expose
	private String callingCode;

	@SerializedName("mobileNo")
	@Expose
	private String mobileNo;

	@SerializedName("guid")
	@Expose
	private String guid;


    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

	public String getCallingCode() {
		return callingCode;
	}

	public void setCallingCode(String callingCode) {
		this.callingCode = callingCode;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
}
