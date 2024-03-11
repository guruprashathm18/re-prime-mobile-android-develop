package com.royalenfield.reprime.models.response.web.ourworldevent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OurWorldEventEntity {

    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("thumbnailImage")
    @Expose
    private String thumbnailImage;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("noOfDays")
    @Expose
    private String noOfDays;
    @SerializedName("noOfBikersRegistered")
    @Expose
    private String noOfBikersRegistered;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getNoOfBikersRegistered() {
        return noOfBikersRegistered;
    }

    public void setNoOfBikersRegistered(String noOfBikersRegistered) {
        this.noOfBikersRegistered = noOfBikersRegistered;
    }

}
