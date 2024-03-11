package com.royalenfield.reprime.models.response.common;

import com.royalenfield.firestore.userinfo.UserInfoResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.models.response.web.dealerride.Dealer;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailsResponse;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;

import java.util.List;

/**
 * @author BOP1KOR on 1/7/2019.
 * <p>
 * Stores the Profile data fetched from server, Dealer list, filtered lsit, List of motorcycles from DMS, And their vehicle details.
 */

public class REUserModelStore {

    //Single Instance Object
    private static REUserModelStore modelStore;
    //Profile details from the RE Website.
    private ProfileData profileData;
    //Profile details from the firestore.
    private UserInfoResponse userInfoResponse;

/*    //Marquee Ride list fetched from the Website server.
    private MarqueeRidesResponse marqueeRidesResponseList;
    //Popular Ride list fetched from the Website server.
    private PopularRidesResponse popularRidesResponseList;
    //OurWorld News info fetched from the Website server.
    private OurWorldNewsResponse ourWorldNewsResponse;
    //OurWorld Event info fetched from the Website server.
    private OurWorldEventResponse ourWorldEventResponse;*/
    //Dealer Ride List fetched from the Website server.
    private List<Dealer> dealerRideList;
    //VehicleDetails List fetched from firestore
    private List<VehicleDetailsResponse> mVehicleDetailsList;

    private List<ProfileRidesResponse> upcomingRideInfoResponse;

    private List<ProfileRidesResponse> pendingRideInfoResponse;

    private List<ProfileRidesResponse> rejectedRideInfoResponse;

    private List<ProfileRidesResponse> pastRideResponse;

    private List<ProfileRidesResponse> ongoingRideResponse;

    //User details fetched from login/signup/firestore
    private String proxyAccessToken;

    private String webAccessToken;
    //User details fetched from login/signup/firestore



    private String refreshToken;

    private String userId = "";

    private String aboutMe = "";

    private String bikename = "";

    private String city = "";

    private String coverImage = "";

    private String dob = "";

    private String email = "";

    private String gender = "";

    private String phoneNo = "";

    private String callingCde = "";

    private String profileUrl = "";

    private String fname;

    private String lname;

    private double latitude;

    private double longitude;

//    public UserInfoResponse getUserInfoResponse() {
//        return userInfoResponse;
//    }
//
//    public void setUserInfoResponse(UserInfoResponse userInfoResponse) {
//        this.userInfoResponse = userInfoResponse;
//    }
//
//
//    public static REUserModelStore getModelStore() {
//        return modelStore;
//    }
//
//    public static void setModelStore(REUserModelStore modelStore) {
//        REUserModelStore.modelStore = modelStore;
//    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static REUserModelStore getInstance() {
        if (modelStore == null) {
            modelStore = new REUserModelStore();
        }
        return modelStore;
    }

    /**
     * Clear the model store.
     */
    public static void clearModelStore() {
        modelStore = null;
    }


    public UserInfoResponse getUserInfoResponse() {
        return userInfoResponse;
    }

    public void setUserInfoResponse(UserInfoResponse userInfoResponse) {
        this.userInfoResponse = userInfoResponse;
    }


    public static REUserModelStore getModelStore() {
        return modelStore;
    }

    public static void setModelStore(REUserModelStore modelStore) {
        REUserModelStore.modelStore = modelStore;
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getBikename() {
        return bikename;
    }

    public void setBikename(String bikename) {
        this.bikename = bikename;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * Gets the VehicleDetailsList from firestore
     *
     * @return : List<VehicleDetailsResponse>
     */
    public List<VehicleDetailsResponse> getVehicleDetailsList() {
        return mVehicleDetailsList;
    }

    /**
     * Sets the VehicleDetailsList to the model fetched from firestorel
     *
     * @param mVehicleDetailsList : List<VehicleDetailsResponse>
     */
    public void setVehicleDetailsList(List<VehicleDetailsResponse> mVehicleDetailsList) {
        this.mVehicleDetailsList = mVehicleDetailsList;
    }

    public List<ProfileRidesResponse> getUpcomingRideResponse() {
        return upcomingRideInfoResponse;
    }

    public void setUpcomingRideResponse(List<ProfileRidesResponse> upcomingRideInfoResponse) {
        this.upcomingRideInfoResponse = upcomingRideInfoResponse;
    }

    public List<ProfileRidesResponse> getPendingRideResponse() {
        return pendingRideInfoResponse;
    }

    public void setPendingRideResponse(List<ProfileRidesResponse> pendingRideInfoResponse) {
        this.pendingRideInfoResponse = pendingRideInfoResponse;
    }

    public List<ProfileRidesResponse> getRejectedRideResponse() {
        return rejectedRideInfoResponse;
    }

    public void setRejectedRideResponse(List<ProfileRidesResponse> rejectedRideInfoResponse) {
        this.rejectedRideInfoResponse = rejectedRideInfoResponse;
    }

    public List<ProfileRidesResponse> getPastRideResponse() {
        return pastRideResponse;
    }

    public void setPastRideResponse(List<ProfileRidesResponse> pastRideResponse) {
        this.pastRideResponse = pastRideResponse;
    }

    public List<ProfileRidesResponse> getOngoingRideResponse() {
        return ongoingRideResponse;
    }

    public void setOngoingRideResponse(List<ProfileRidesResponse> ongoingRideResponse) {
        this.ongoingRideResponse = ongoingRideResponse;
    }


    public String getProxyAccessToken() {
        return proxyAccessToken;
    }

    public void setProxyAccessToken(String proxyAccessToken) {
        this.proxyAccessToken = proxyAccessToken;
    }

    public String getWebAccessToken() {
        return webAccessToken;
    }

    public void setWebAccessToken(String webAccessToken) {
        this.webAccessToken = webAccessToken;
    }


    public String getCallingCde() {
        return callingCde;
    }

    public void setCallingCde(String callingCde) {
        this.callingCde = callingCde;
    }
}
