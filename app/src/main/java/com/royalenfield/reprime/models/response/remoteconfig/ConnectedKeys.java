package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectedKeys {

    @SerializedName("fuelThreshold")
    @Expose
    private String fuelThreshold;
    @SerializedName("batteryThreshold")
    @Expose
    private String batteryThreshold;
    @SerializedName("authKey")
    @Expose
    private String authKey;

    @SerializedName("obd_refresh_interval")
    @Expose
    private String obdRefreshInterval="60";

    @SerializedName("other_refresh_interval")
    @Expose
    private String otherRefreshInterval;

    @SerializedName("health_alert_count")
    @Expose
    private String healthAlertCount;
    @SerializedName("trip_count")
    @Expose
    private String tripCount;

    @SerializedName("gsm_threshold")
    @Expose
    private String gsmThreshold="120";

	@SerializedName("customer_care")
	@Expose
	private String customerCare="";


    public String getGsmThreshold() {
        return gsmThreshold;
    }

    public void setGsmThreshold(String gsmThreshold) {
        this.gsmThreshold = gsmThreshold;
    }

    public String getFuelThreshold() {
        return fuelThreshold;
    }

    public void setFuelThreshold(String fuelThreshold) {
        this.fuelThreshold = fuelThreshold;
    }

    public String getBatteryThreshold() {
        return batteryThreshold;
    }

    public void setBatteryThreshold(String batteryThreshold) {
        this.batteryThreshold = batteryThreshold;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getObdRefreshInterval() {
        return obdRefreshInterval;
    }

    public void setObdRefreshInterval(String obdRefreshInterval) {
        this.obdRefreshInterval = obdRefreshInterval;
    }

    public String getOtherRefreshInterval() {
        return otherRefreshInterval;
    }

    public void setOtherRefreshInterval(String otherRefreshInterval) {
        this.otherRefreshInterval = otherRefreshInterval;
    }

    public String getHealthAlertCount() {
        return healthAlertCount;
    }

    public void setHealthAlertCount(String healthAlertCount) {
        this.healthAlertCount = healthAlertCount;
    }

    public String getTripCount() {
        return tripCount;
    }

    public void setTripCount(String tripCount) {
        this.tripCount = tripCount;
    }

	public String getCustomerCare() {
		return customerCare;
	}

	public void setCustomerCare(String customerCare) {
		this.customerCare = customerCare;
	}
}
