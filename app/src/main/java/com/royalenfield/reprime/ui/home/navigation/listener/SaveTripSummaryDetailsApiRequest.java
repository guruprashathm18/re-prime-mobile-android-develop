package com.royalenfield.reprime.ui.home.navigation.listener;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SaveTripSummaryDetailsApiRequest {

    @SerializedName("userGUID")
    @Expose
    private String userGUID;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("startPoint")
    @Expose
    private SaveTripSummaryStartPoint startPoint;
    @SerializedName("endPoint")
    @Expose
    private SaveTripSummaryEndPoint endPoint;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("deviceOS")
    @Expose
    private String deviceOS;
    @SerializedName("avgSpeed")
    @Expose
    private String avgSpeed;
    @SerializedName("totalTime")
    @Expose
    private String totalTime;
    @SerializedName("totalDistance")
    @Expose
    private String totalDistance;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;

    /**
     * No args constructor for use in serialization
     *
     */
    public SaveTripSummaryDetailsApiRequest() {
    }

    /**
     *
     * @param endPoint
     * @param userGUID
     * @param serialNumber
     * @param deviceOS
     * @param totalTime
     * @param startPoint
     * @param name
     * @param startTime
     * @param endTime
     * @param totalDistance
     * @param avgSpeed
     */
    public SaveTripSummaryDetailsApiRequest(String userGUID, String serialNumber, SaveTripSummaryStartPoint startPoint, SaveTripSummaryEndPoint endPoint, String name, String deviceOS, String avgSpeed, String totalTime, String totalDistance, String startTime, String endTime) {
        super();
        this.userGUID = userGUID;
        this.serialNumber = serialNumber;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.name = name;
        this.deviceOS = deviceOS;
        this.avgSpeed = avgSpeed;
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getUserGUID() {
        return userGUID;
    }

    public void setUserGUID(String userGUID) {
        this.userGUID = userGUID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public SaveTripSummaryStartPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(SaveTripSummaryStartPoint startPoint) {
        this.startPoint = startPoint;
    }

    public SaveTripSummaryEndPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(SaveTripSummaryEndPoint endPoint) {
        this.endPoint = endPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SaveTripSummaryDetailsApiRequest{" +
                "userGUID='" + userGUID + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", name='" + name + '\'' +
                ", deviceOS='" + deviceOS + '\'' +
                ", avgSpeed='" + avgSpeed + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", totalDistance='" + totalDistance + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
