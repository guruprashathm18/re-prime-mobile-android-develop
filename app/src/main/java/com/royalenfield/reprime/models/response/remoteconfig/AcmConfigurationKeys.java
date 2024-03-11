package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcmConfigurationKeys {

    @SerializedName("androidIntegrationKey")
    @Expose
    private String integrationKey;
    @SerializedName("marketingHost")
    @Expose
    private String marketingURL;
    @SerializedName("trackingHost")
    @Expose
    private String trackingURL;

    public String getIntegrationKey() {
        return integrationKey;
    }

    public void setIntegrationKey(String integrationKey) {
        this.integrationKey = integrationKey;
    }

    public String getMarketingURL() {
        return marketingURL;
    }

    public void setMarketingURL(String marketingURL) {
        this.marketingURL = marketingURL;
    }

    public String getTrackingURL() {
        return trackingURL;
    }

    public void setTrackingURL(String trackingURL) {
        this.trackingURL = trackingURL;
    }

}
