package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeatureModel {
    @SerializedName("featureStatus")
    @Expose
    private String featureStatus;
    @SerializedName("releaseFeatureStatus")
    @Expose
    private String releaseFeatureStatus;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("featureImageURL")
    @Expose
    private String featureImageURL;
    @SerializedName("tbtReleaseFeatureStatus")
    @Expose
    private String tbtReleaseFeatureStatus;

    public String getFeatureStatus() {
        return featureStatus;
    }

    public void setFeatureStatus(String featureStatus) {
        this.featureStatus = featureStatus;
    }

    public String getReleaseFeatureStatus() {
        return releaseFeatureStatus;
    }

    public void setReleaseFeatureStatus(String releaseFeatureStatus) {
        this.releaseFeatureStatus = releaseFeatureStatus;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFeatureImageURL() {
        return featureImageURL;
    }

    public void setFeatureImageURL(String featureImageURL) {
        this.featureImageURL = featureImageURL;
    }

    public String getTbtReleaseFeatureStatus() {
        return tbtReleaseFeatureStatus;
    }

    public void setTbtReleaseFeatureStatus(String tbtReleaseFeatureStatus) {
        this.tbtReleaseFeatureStatus = tbtReleaseFeatureStatus;
    }
}
