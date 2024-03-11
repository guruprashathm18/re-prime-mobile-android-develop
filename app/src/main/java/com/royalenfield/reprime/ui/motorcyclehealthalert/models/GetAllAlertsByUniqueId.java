package com.royalenfield.reprime.ui.motorcyclehealthalert.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllAlertsByUniqueId {

    @SerializedName("clientid")
    @Expose
    private Integer clientid;
    @SerializedName("uniqueid")
    @Expose
    private String uniqueid;
    @SerializedName("alerttype")
    @Expose
    private String alerttype;
    @SerializedName("alertvalue")
    @Expose
    private String alertvalue;

    @SerializedName("alertPriority")
    @Expose
    private String alertPriority;

    @SerializedName("from_ts")
    @Expose
    private String fromTs;
    @SerializedName("to_ts")
    @Expose
    private String toTs;
    @SerializedName("server_ts")
    @Expose
    private String serverTs;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("to_lat")
    @Expose
    private Double toLat;
    @SerializedName("to_lng")
    @Expose
    private Double toLng;
    @SerializedName("alert_completed")
    @Expose
    private Boolean alertCompleted;
    @SerializedName("view_status")
    @Expose
    private Boolean viewStatus;
    @SerializedName("pluscode")
    @Expose
    private String pluscode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;
    @SerializedName("alertName")
    @Expose
    private String alertName;
    @SerializedName("alertDescription")
    @Expose
    private String alertDescription;

    public Integer getClientid() {
        return clientid;
    }

    public void setClientid(Integer clientid) {
        this.clientid = clientid;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getAlerttype() {
        return alerttype;
    }

    public void setAlerttype(String alerttype) {
        this.alerttype = alerttype;
    }

    public String getAlertvalue() {
        return alertvalue;
    }

    public void setAlertvalue(String alertvalue) {
        this.alertvalue = alertvalue;
    }

    public String getFromTs() {
        return fromTs;
    }

    public void setFromTs(String fromTs) {
        this.fromTs = fromTs;
    }

    public String getToTs() {
        return toTs;
    }

    public void setToTs(String toTs) {
        this.toTs = toTs;
    }

    public String getServerTs() {
        return serverTs;
    }

    public void setServerTs(String serverTs) {
        this.serverTs = serverTs;
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

    public Double getToLat() {
        return toLat;
    }

    public void setToLat(Double toLat) {
        this.toLat = toLat;
    }

    public Double getToLng() {
        return toLng;
    }

    public void setToLng(Double toLng) {
        this.toLng = toLng;
    }

    public Boolean getAlertCompleted() {
        return alertCompleted;
    }

    public void setAlertCompleted(Boolean alertCompleted) {
        this.alertCompleted = alertCompleted;
    }

    public Boolean getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(Boolean viewStatus) {
        this.viewStatus = viewStatus;
    }

    public String getPluscode() {
        return pluscode;
    }

    public void setPluscode(String pluscode) {
        this.pluscode = pluscode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public void setAlertDescription(String alertDescription) {
        this.alertDescription = alertDescription;
    }

    public String getAlertPriority() {
        return alertPriority;
    }

    public void setAlertPriority(String alertPriority) {
        this.alertPriority = alertPriority;
    }
}
