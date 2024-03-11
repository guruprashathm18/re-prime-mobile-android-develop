package com.royalenfield.reprime.models.response.web.UpdateProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("mediaRole")
    @Expose
    private MediaRole mediaRole;
    @SerializedName("userUrl")
    @Expose
    private String userUrl;
    @SerializedName("userInterest")
    @Expose
    private Object userInterest;
    @SerializedName("listofTags")
    @Expose
    private List<Object> listofTags = null;
    @SerializedName("jwtAccessToken")
    @Expose
    private Object jwtAccessToken;
    @SerializedName("loginStatus")
    @Expose
    private Boolean loginStatus;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("socialNetworkUrls")
    @Expose
    private SocialNetworkUrls socialNetworkUrls;
    @SerializedName("favouriteRideExperience")
    @Expose
    private Object favouriteRideExperience;
    @SerializedName("favouriteQuote")
    @Expose
    private Object favouriteQuote;
    @SerializedName("discussionJoined")
    @Expose
    private List<Object> discussionJoined = null;
    @SerializedName("ridesJoined")
    @Expose
    private List<Object> ridesJoined = null;
    @SerializedName("tripStoriesCreated")
    @Expose
    private List<Object> tripStoriesCreated = null;
    @SerializedName("ridesCreated")
    @Expose
    private List<Object> ridesCreated = null;
    @SerializedName("ownedBikeInfo")
    @Expose
    private List<Object> ownedBikeInfo = null;
    @SerializedName("bikeOwned")
    @Expose
    private List<Object> bikeOwned = null;
    @SerializedName("isRoyalEnfieldOwner")
    @Expose
    private String isRoyalEnfieldOwner;
    @SerializedName("emailToken")
    @Expose
    private Double emailToken;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("emailVerified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("mobileVerified")
    @Expose
    private Boolean mobileVerified;
    @SerializedName("reviewCreated")
    @Expose
    private List<Object> reviewCreated = null;
    @SerializedName("ownBike")
    @Expose
    private String ownBike;
    @SerializedName("addressInfo")
    @Expose
    private AddressInfo addressInfo;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("coverImage")
    @Expose
    private String coverImage;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("bikename")
    @Expose
    private Object bikename;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("locality")
    @Expose
    private Locality locality;
    @SerializedName("previousUserId")
    @Expose
    private Object previousUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public MediaRole getMediaRole() {
        return mediaRole;
    }

    public void setMediaRole(MediaRole mediaRole) {
        this.mediaRole = mediaRole;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public Object getUserInterest() {
        return userInterest;
    }

    public void setUserInterest(Object userInterest) {
        this.userInterest = userInterest;
    }

    public List<Object> getListofTags() {
        return listofTags;
    }

    public void setListofTags(List<Object> listofTags) {
        this.listofTags = listofTags;
    }

    public Object getJwtAccessToken() {
        return jwtAccessToken;
    }

    public void setJwtAccessToken(Object jwtAccessToken) {
        this.jwtAccessToken = jwtAccessToken;
    }

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public SocialNetworkUrls getSocialNetworkUrls() {
        return socialNetworkUrls;
    }

    public void setSocialNetworkUrls(SocialNetworkUrls socialNetworkUrls) {
        this.socialNetworkUrls = socialNetworkUrls;
    }

    public Object getFavouriteRideExperience() {
        return favouriteRideExperience;
    }

    public void setFavouriteRideExperience(Object favouriteRideExperience) {
        this.favouriteRideExperience = favouriteRideExperience;
    }

    public Object getFavouriteQuote() {
        return favouriteQuote;
    }

    public void setFavouriteQuote(Object favouriteQuote) {
        this.favouriteQuote = favouriteQuote;
    }

    public List<Object> getDiscussionJoined() {
        return discussionJoined;
    }

    public void setDiscussionJoined(List<Object> discussionJoined) {
        this.discussionJoined = discussionJoined;
    }

    public List<Object> getRidesJoined() {
        return ridesJoined;
    }

    public void setRidesJoined(List<Object> ridesJoined) {
        this.ridesJoined = ridesJoined;
    }

    public List<Object> getTripStoriesCreated() {
        return tripStoriesCreated;
    }

    public void setTripStoriesCreated(List<Object> tripStoriesCreated) {
        this.tripStoriesCreated = tripStoriesCreated;
    }

    public List<Object> getRidesCreated() {
        return ridesCreated;
    }

    public void setRidesCreated(List<Object> ridesCreated) {
        this.ridesCreated = ridesCreated;
    }

    public List<Object> getOwnedBikeInfo() {
        return ownedBikeInfo;
    }

    public void setOwnedBikeInfo(List<Object> ownedBikeInfo) {
        this.ownedBikeInfo = ownedBikeInfo;
    }

    public List<Object> getBikeOwned() {
        return bikeOwned;
    }

    public void setBikeOwned(List<Object> bikeOwned) {
        this.bikeOwned = bikeOwned;
    }

    public String getIsRoyalEnfieldOwner() {
        return isRoyalEnfieldOwner;
    }

    public void setIsRoyalEnfieldOwner(String isRoyalEnfieldOwner) {
        this.isRoyalEnfieldOwner = isRoyalEnfieldOwner;
    }

    public Double getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(Double emailToken) {
        this.emailToken = emailToken;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(Boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public List<Object> getReviewCreated() {
        return reviewCreated;
    }

    public void setReviewCreated(List<Object> reviewCreated) {
        this.reviewCreated = reviewCreated;
    }

    public String getOwnBike() {
        return ownBike;
    }

    public void setOwnBike(String ownBike) {
        this.ownBike = ownBike;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getBikename() {
        return bikename;
    }

    public void setBikename(Object bikename) {
        this.bikename = bikename;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public Object getPreviousUserId() {
        return previousUserId;
    }

    public void setPreviousUserId(Object previousUserId) {
        this.previousUserId = previousUserId;
    }

}
