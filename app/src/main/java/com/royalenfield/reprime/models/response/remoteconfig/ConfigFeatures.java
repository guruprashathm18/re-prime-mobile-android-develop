package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigFeatures {

    @SerializedName("createRide")
    @Expose
    private FeatureModel createRide;
    @SerializedName("serviceBooking")
    @Expose
    private FeatureModel serviceBooking;
    @SerializedName("bookServiceAppointment")
    @Expose
    private FeatureModel bookServiceAppointment;
    @SerializedName("TBT")
    @Expose
    private FeatureModel TBT;
    @SerializedName("communityevents")
    @Expose
    private FeatureModel communityevents;
    @SerializedName("communityrides")
    @Expose
    private FeatureModel communityrides;
    @SerializedName("recordroute")
    @Expose
    private FeatureModel recordroute;

    @SerializedName("DRSA")
    @Expose
    private FeatureModel drsa;

    public FeatureModel getDrsa() {
        return drsa;
    }

    public void setDrsa(FeatureModel drsa) {
        this.drsa = drsa;
    }

    public FeatureModel getRecordRoute() {
        return recordroute;
    }

    public void setRecordRoute(FeatureModel recordroute) {
        this.recordroute = recordroute;
    }


    public FeatureModel getCreateRide() {
        return createRide;
    }

    public void setCreateRide(FeatureModel createRide) {
        this.createRide = createRide;
    }

    public FeatureModel getServiceBooking() {
        return serviceBooking;
    }

    public void setServiceBooking(FeatureModel serviceBooking) {
        this.serviceBooking = serviceBooking;
    }

    public FeatureModel getBookServiceAppointment() {
        return bookServiceAppointment;
    }

    public void setBookServiceAppointment(FeatureModel bookServiceAppointment) {
        this.bookServiceAppointment = bookServiceAppointment;
    }

    public FeatureModel getTBT() {
        return TBT;
    }

    public void setTBT(FeatureModel tBT) {
        this.TBT = tBT;
    }


    public FeatureModel getCommunityevents() {
        return communityevents;
    }

    public void setCommunityevents(FeatureModel communityevents) {
        this.communityevents = communityevents;
    }

    public FeatureModel getCommunityrides() {
        return communityrides;
    }

    public void setCommunityrides(FeatureModel communityrides) {
        this.communityrides = communityrides;
    }
}



