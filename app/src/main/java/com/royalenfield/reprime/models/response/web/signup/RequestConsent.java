package com.royalenfield.reprime.models.response.web.signup;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RequestConsent {

    @SerializedName("terms_and_conditions")
    @Expose
    private Boolean termsAndConditions;
    @SerializedName("promotionsnews")
    @Expose
    private Boolean promotionsnews;
    @SerializedName("surveysandresearch")
    @Expose
    private Boolean surveysandresearch;
    @SerializedName("communityactivities")
    @Expose
    private Boolean communityactivities;
    @SerializedName("allpromotions")
    @Expose
    private Boolean allpromotions;
    @SerializedName("donotphone")
    @Expose
    private Boolean donotphone;
    @SerializedName("donotemail")
    @Expose
    private Boolean donotemail;
    @SerializedName("createdAt")
    @Expose
    private Object createdAt;
    @SerializedName("updatedAt")
    @Expose
    private Object updatedAt;

    @SerializedName("donotwhatsapp")
    @Expose
    private Boolean donotwhatsapp;

    @SerializedName("donotpushnotification")
    @Expose
    private Boolean donotpushnotification;

    public Boolean getDonotwhatsapp() {
        return donotwhatsapp;
    }

    public void setDonotwhatsapp(Boolean donotwhatsapp) {
        this.donotwhatsapp = donotwhatsapp;
    }

    public Boolean getDonotpushnotification() {
        return donotpushnotification;
    }

    public void setDonotpushnotification(Boolean donotpushnotification) {
        this.donotpushnotification = donotpushnotification;
    }

    public Boolean getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(Boolean termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Boolean getPromotionsnews() {
        return promotionsnews;
    }

    public void setPromotionsnews(Boolean promotionsnews) {
        this.promotionsnews = promotionsnews;
    }

    public Boolean getSurveysandresearch() {
        return surveysandresearch;
    }

    public void setSurveysandresearch(Boolean surveysandresearch) {
        this.surveysandresearch = surveysandresearch;
    }

    public Boolean getCommunityactivities() {
        return communityactivities;
    }

    public void setCommunityactivities(Boolean communityactivities) {
        this.communityactivities = communityactivities;
    }

    public Boolean getAllpromotions() {
        return allpromotions;
    }

    public void setAllpromotions(Boolean allpromotions) {
        this.allpromotions = allpromotions;
    }

    public Boolean getDonotphone() {
        return donotphone;
    }

    public void setDonotphone(Boolean donotphone) {
        this.donotphone = donotphone;
    }

    public Boolean getDonotemail() {
        return donotemail;
    }

    public void setDonotemail(Boolean donotemail) {
        this.donotemail = donotemail;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static RequestConsent copy( RequestConsent other ) {
        RequestConsent newUser = new RequestConsent();
        newUser.allpromotions = other.allpromotions;
        newUser.termsAndConditions = other.termsAndConditions;
        newUser.promotionsnews = other.promotionsnews;
        newUser.surveysandresearch = other.surveysandresearch;
        newUser.communityactivities = other.communityactivities;
        newUser.createdAt = other.createdAt;
        newUser.updatedAt = other.updatedAt;
        newUser.donotemail = other.donotemail;
        newUser.donotphone = other.donotphone;
        newUser.donotwhatsapp = other.donotwhatsapp;
        newUser.donotpushnotification = other.donotpushnotification;
        //... etc.
        return newUser;
    }
}
