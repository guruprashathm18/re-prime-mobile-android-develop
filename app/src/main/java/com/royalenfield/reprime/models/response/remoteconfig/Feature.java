package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feature {

    @SerializedName("guest_login")
    @Expose
    private String guestLogin;
    @SerializedName("community")
    @Expose
    private String community;
    @SerializedName("motorcycle_info")
    @Expose
    private String motorcycleInfo;
    @SerializedName("MIY")
    @Expose
    private String mIY;
    @SerializedName("FAQ")
    @Expose
    private String faq;
    @SerializedName("connected_bike")
    @Expose
    private String connectedBike;
    @SerializedName("edit_primary_no")
    @Expose
    private String editPrimaryNo;
    @SerializedName("wanderlust")
    @Expose
    private String wanderlust;
    @SerializedName("trips")
    @Expose
    private String trips;
    @SerializedName("create_ride")
    @Expose
    private String is_rides_enabled;
    @SerializedName("community_rides")
    @Expose
    private String is_community_rides_enabled;
    @SerializedName("community_events")
    @Expose
    private String is_community_events_enabled;
    @SerializedName("geo_query")
    @Expose
    private String is_geo_query_enabled;
    @SerializedName("bctV2")
    @Expose
    private String isBCTEnabled;
    @SerializedName("tripperCallNotification")
    @Expose
    private String tripperCallNotification;
    @SerializedName("showRideWeb")
    @Expose
    private String showRideWeb;
    @SerializedName("showOurWorldWeb")
    @Expose
    private String showOurWorldWeb;
    @SerializedName("showRideWebV2")
    @Expose
    private String showRideWebV2="N";
    @SerializedName("showOurWorldWebV2")
    @Expose
    private String showOurWorldWebV2="N";
    @SerializedName("show_track_order")
    @Expose
    private String showTrackOrder="N";
    @SerializedName("showConsentControlsV2")
    @Expose
    private String showConsentControlsV2="Y";


    public String getShowConsentControlsV2() {
        return showConsentControlsV2;
    }

    public void setShowConsentControlsV2(String showConsentControlsV2) {
        this.showConsentControlsV2 = showConsentControlsV2;
    }

    //    public String getShowRideWeb() {
//        return showRideWeb;
//    }
//
//    public void setShowRideWeb(String showRideWeb) {
//        this.showRideWeb = showRideWeb;
//    }
//
//    public String getShowOurWorldWeb() {
//        return showOurWorldWeb;
//    }
//
//    public void setShowOurWorldWeb(String showOurWorldWeb) {
//        this.showOurWorldWeb = showOurWorldWeb;
//    }
    public String getTripperCallNotification() {
        return tripperCallNotification;
    }
    public void setTripperCallNotification(String tripperCallNotification) {
        this.tripperCallNotification = tripperCallNotification;
    }

    public String getIs_rides_enabled() {
        return is_rides_enabled;
    }

    public void setIs_rides_enabled(String is_rides_enabled) {
        this.is_rides_enabled = is_rides_enabled;
    }

    public String getIs_geo_query_enabled() {
        return is_geo_query_enabled;
    }

    @SerializedName("showDataSanitization")
    @Expose
    private String showDataSanitization;

    @SerializedName("showOTPScreen")
    @Expose
    private String showOTPScreen;

    @SerializedName("showEditProfile")
    @Expose
    private String showEditProfile;

    @SerializedName("showOTPEmailText")
    @Expose
    private String showOTPEmailText;

    @SerializedName("showSparesGenuinityCheck")
    @Expose
    private String showSparesGenuinityCheck;


    public String getShowOTPEmailText() {
        return showOTPEmailText;
    }

    public void setShowOTPEmailText(String showOTPEmailText) {
        this.showOTPEmailText = showOTPEmailText;
    }
    public void setIs_geo_query_enabled(String is_geo_query_enabled) {
        this.is_geo_query_enabled = is_geo_query_enabled;
    }
    public String getWanderlust() {
        return wanderlust;
    }


    public void setWanderlust(String wanderlust) {
        this.wanderlust = wanderlust;
    }

    public String getTrips() {
        return trips;
    }

    public void setTrips(String trips) {
        this.trips = trips;
    }


    public String getShowDataSanitization() {
        return showDataSanitization;
    }

    public void setShowDataSanitization(String showDataSanitization) {
        this.showDataSanitization = showDataSanitization;
    }

    public String getShowOTPScreen() {
        return showOTPScreen;
    }

    public void setShowOTPScreen(String showOTPScreen) {
        this.showOTPScreen = showOTPScreen;
    }

    public String getShowEditProfile() {
        return showEditProfile;
    }

    public void setShowEditProfile(String showEditProfile) {
        this.showEditProfile = showEditProfile;
    }

    @SerializedName("showConsentControls")
    @Expose
    private String showConsentControls;

    public String getShowConsentControls() {
        return showConsentControls;
    }

    public void setShowConsentControls(String showConsentControls) {
        this.showConsentControls = showConsentControls;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public String getGuestLogin() {
        return guestLogin;
    }

    public void setGuestLogin(String guestLogin) {
        this.guestLogin = guestLogin;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getMotorcycleInfo() {
        return motorcycleInfo;
    }

    public void setMotorcycleInfo(String motorcycleInfo) {
        this.motorcycleInfo = motorcycleInfo;
    }

    public String getMIY() {
        return mIY;
    }

    public void setMIY(String mIY) {
        this.mIY = mIY;
    }

    public String getConnectedBike() {
        return connectedBike;
    }

    public void setConnectedBike(String connectedBike) {
        this.connectedBike = connectedBike;
    }

    public String getEditPrimaryNo() {
        return editPrimaryNo;
    }

    public void setEditPrimaryNo(String editPrimaryNo) {
        this.editPrimaryNo = editPrimaryNo;
    }

    public String getIs_community_rides_enabled() {
        return is_community_rides_enabled;
    }

    public void setIs_community_rides_enabled(String is_community_rides_enabled) {
        this.is_community_rides_enabled = is_community_rides_enabled;
    }

    public String getIs_community_events_enabled() {
        return is_community_events_enabled;
    }

    public void setIs_community_events_enabled(String is_community_events_enabled) {
        this.is_community_events_enabled = is_community_events_enabled;
    }

    public String getIsBCTEnabled() {
        return isBCTEnabled;
       // return "Y";
    }

    public void setIsBCTEnabled(String isBCTEnabled) {
        this.isBCTEnabled = isBCTEnabled;
    }

    public String getShowSparesGenuinityCheck() {
        return showSparesGenuinityCheck;
    }

    public void setShowSparesGenuinityCheck(String showSparesGenuinityCheck) {
        this.showSparesGenuinityCheck = showSparesGenuinityCheck;
    }

    public String getShowRideWebV2() {
        return showRideWebV2;
    }

    public void setShowRideWebV2(String showRideWebV2) {
        this.showRideWebV2 = showRideWebV2;
    }

    public String getShowOurWorldWebV2() {
        return showOurWorldWebV2;
    }

    public void setShowOurWorldWebV2(String showOurWorldWebV2) {
        this.showOurWorldWebV2 = showOurWorldWebV2;
    }

    public String getShowTrackOrder() {
        return showTrackOrder;
    }

    public void setShowTrackOrder(String showTrackOrder) {
        this.showTrackOrder = showTrackOrder;
    }
}