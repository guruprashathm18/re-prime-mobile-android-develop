package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class FeatureList {
    @SerializedName("default")
    @Expose
    private Feature defaultFeatures;
    @SerializedName("IN")
    @Expose
    private Feature iN;
    @SerializedName("TH")
    @Expose
    private Feature th;


    public Feature getIN() {
        return iN;
    }

    public void setIN(Feature iN) {
        this.iN = iN;
    }

    public Feature getTh() {
        return th;
    }

    public void setTh(Feature th) {
        this.th = th;
    }

    public Feature getDefaultFeatures() {
        return defaultFeatures;
    }

    public void setDefaultFeatures(Feature defaultFeatures) {
        this.defaultFeatures = defaultFeatures;
    }
}