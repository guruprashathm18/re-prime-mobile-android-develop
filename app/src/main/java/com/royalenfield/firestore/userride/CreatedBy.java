package com.royalenfield.firestore.userride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreatedBy {

    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("bikename")
    @Expose
    private Object bikename;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("coverImage")
    @Expose
    private String coverImage;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("addressInfo")
    @Expose
    private List<Object> addressInfo = null;
    @SerializedName("isRoyalEnfieldOwner")
    @Expose
    private String isRoyalEnfieldOwner;
    @SerializedName("joinedUserRides")
    @Expose
    private List<String> joinedUserRides = null;
    @SerializedName("company")
    @Expose
    private Object company;
    @SerializedName("province")
    @Expose
    private Object province;
    @SerializedName("provinceCode")
    @Expose
    private Object provinceCode;
    @SerializedName("acceptsMarketing")
    @Expose
    private boolean acceptsMarketing;
    @SerializedName("totalSpent")
    @Expose
    private Object totalSpent;
    @SerializedName("totalOrders")
    @Expose
    private Object totalOrders;
    @SerializedName("note")
    @Expose
    private Object note;
    @SerializedName("taxExempt")
    @Expose
    private boolean taxExempt;
    @SerializedName("app_id")
    @Expose
    private int appId;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("lastModified")
    @Expose
    private String lastModified;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

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

    public Object getBikename() {
        return bikename;
    }

    public void setBikename(Object bikename) {
        this.bikename = bikename;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Object> getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(List<Object> addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getIsRoyalEnfieldOwner() {
        return isRoyalEnfieldOwner;
    }

    public void setIsRoyalEnfieldOwner(String isRoyalEnfieldOwner) {
        this.isRoyalEnfieldOwner = isRoyalEnfieldOwner;
    }

    public List<String> getJoinedUserRides() {
        return joinedUserRides;
    }

    public void setJoinedUserRides(List<String> joinedUserRides) {
        this.joinedUserRides = joinedUserRides;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public Object getProvince() {
        return province;
    }

    public void setProvince(Object province) {
        this.province = province;
    }

    public Object getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Object provinceCode) {
        this.provinceCode = provinceCode;
    }

    public boolean isAcceptsMarketing() {
        return acceptsMarketing;
    }

    public void setAcceptsMarketing(boolean acceptsMarketing) {
        this.acceptsMarketing = acceptsMarketing;
    }

    public Object getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Object totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Object getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Object totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

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

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}