package com.royalenfield.reprime.models.response.web.checkin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckInData {

    @SerializedName("checkInThumbnailImage")
    @Expose
    private String checkInThumbnailImage;
    @SerializedName("checkInPlaceName")
    @Expose
    private String checkInPlaceName;
    @SerializedName("checkInDescription")
    @Expose
    private String checkInDescription;
    @SerializedName("checkInCategory")
    @Expose
    private String checkInCategory;
    @SerializedName("rideId")
    @Expose
    private String rideId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("popularity")
    @Expose
    private int popularity;
    @SerializedName("moderationStatus")
    @Expose
    private String moderationStatus;
    @SerializedName("rejectionReason")
    @Expose
    private String rejectionReason;
    @SerializedName("rejectionCount")
    @Expose
    private int rejectionCount;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("lastModified")
    @Expose
    private String lastModified;
    @SerializedName("__v")
    @Expose
    private int v;

    public String getCheckInThumbnailImage() {
        return checkInThumbnailImage;
    }

    public void setCheckInThumbnailImage(String checkInThumbnailImage) {
        this.checkInThumbnailImage = checkInThumbnailImage;
    }

    public String getCheckInPlaceName() {
        return checkInPlaceName;
    }

    public void setCheckInPlaceName(String checkInPlaceName) {
        this.checkInPlaceName = checkInPlaceName;
    }

    public String getCheckInDescription() {
        return checkInDescription;
    }

    public void setCheckInDescription(String checkInDescription) {
        this.checkInDescription = checkInDescription;
    }

    public String getCheckInCategory() {
        return checkInCategory;
    }

    public void setCheckInCategory(String checkInCategory) {
        this.checkInCategory = checkInCategory;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public int getRejectionCount() {
        return rejectionCount;
    }

    public void setRejectionCount(int rejectionCount) {
        this.rejectionCount = rejectionCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

}
