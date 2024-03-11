package com.royalenfield.reprime.models.response.web.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactDetails {

    @SerializedName("mobile")
    @Expose
    private Mobile mobile;
    @SerializedName("emailVerified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("email")
    @Expose
    private String email;

    public Mobile getMobile() {
        return mobile;
    }

    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}