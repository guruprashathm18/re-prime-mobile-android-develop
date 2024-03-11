package com.royalenfield.reprime.models.response.web.dealerride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalInfo {

    @SerializedName("bikeName")
    @Expose
    private Object bikeName;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("phoneNo")
    @Expose
    private Object phoneNo;
    @SerializedName("dob")
    @Expose
    private Object dob;
    @SerializedName("isRoyalEnfieldOwner")
    @Expose
    private Object isRoyalEnfieldOwner;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("lName")
    @Expose
    private Object lName;
    @SerializedName("fName")
    @Expose
    private Object fName;

    public Object getBikeName() {
        return bikeName;
    }

    public void setBikeName(Object bikeName) {
        this.bikeName = bikeName;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(Object phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public Object getIsRoyalEnfieldOwner() {
        return isRoyalEnfieldOwner;
    }

    public void setIsRoyalEnfieldOwner(Object isRoyalEnfieldOwner) {
        this.isRoyalEnfieldOwner = isRoyalEnfieldOwner;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getLName() {
        return lName;
    }

    public void setLName(Object lName) {
        this.lName = lName;
    }

    public Object getFName() {
        return fName;
    }

    public void setFName(Object fName) {
        this.fName = fName;
    }

}
