package com.royalenfield.reprime.models.response.remoteconfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteConfigData {
    @SerializedName("acm_configuration")
    @Expose
    private String acm_configuration;
    @SerializedName("app_settings")
    @Expose
    private String app_settings;
    @SerializedName("app_update_interval")
    @Expose
    private String app_update_interval;
    @SerializedName("configurable_features")
    @Expose
    private String configurable_features;
    @SerializedName("connected_keys")
    @Expose
    private String connected_keys;
    @SerializedName("firebase_urls")
    @Expose
    private String firebase_urls;
    @SerializedName("google_keys")
    @Expose
    private String google_keys;
    @SerializedName("isDummySlotsEnabled")
    @Expose
    private String isDummySlotsEnabled;
    @SerializedName("logger_service")
    @Expose
    private String logger_service;

    @SerializedName("mobile_base_urls")
    @Expose
    private String mobile_base_urls;
    @SerializedName("re_keys")
    @Expose
    private String re_keys;
    @SerializedName("re_prime_app_minimum_android_version")
    @Expose
    private String re_prime_app_minimum_android_version;
    @SerializedName("rides_keys")
    @Expose
    private String rides_keys;

    @SerializedName("navigation_keys")
    @Expose
    private String navigation_keys;

    @SerializedName("serviceCentersLWPURL")
    @Expose
    private String serviceCentersLWPURL;
    @SerializedName("servicebookingCutOffTime")
    @Expose
    private String servicebookingCutOffTime;

    @SerializedName("twoWheelerCountries")
    @Expose
    private String twoWheelerCountries;
    @SerializedName("global_validations")
    @Expose
    private String global_validations;
    @SerializedName("feature_status")
    @Expose
    private String feature_status;


    @SerializedName("version")
    @Expose
    private String version;

    public String getAcm_configuration() {
        return acm_configuration;
    }

    public void setAcm_configuration(String acm_configuration) {
        this.acm_configuration = acm_configuration;
    }

    public String getApp_settings() {
        return app_settings;
    }

    public void setApp_settings(String app_settings) {
        this.app_settings = app_settings;
    }

    public String getApp_update_interval() {
        return app_update_interval;
    }

    public void setApp_update_interval(String app_update_interval) {
        this.app_update_interval = app_update_interval;
    }

    public String getConfigurable_features() {
        return configurable_features;
    }

    public void setConfigurable_features(String configurable_features) {
        this.configurable_features = configurable_features;
    }

    public String getConnected_keys() {
        return connected_keys;
    }

    public void setConnected_keys(String connected_keys) {
        this.connected_keys = connected_keys;
    }

    public String getFirebase_urls() {
        return firebase_urls;
    }

    public void setFirebase_urls(String firebase_urls) {
        this.firebase_urls = firebase_urls;
    }

    public String getGoogle_keys() {
        return google_keys;
    }

    public void setGoogle_keys(String google_keys) {
        this.google_keys = google_keys;
    }

    public String getIsDummySlotsEnabled() {
        return isDummySlotsEnabled;
    }

    public void setIsDummySlotsEnabled(String isDummySlotsEnabled) {
        this.isDummySlotsEnabled = isDummySlotsEnabled;
    }

    public String getLogger_service() {
        return logger_service;
    }

    public void setLogger_service(String logger_service) {
        this.logger_service = logger_service;
    }

    public String getMobile_base_urls() {
        return mobile_base_urls;
    }

    public void setMobile_base_urls(String mobile_base_urls) {
        this.mobile_base_urls = mobile_base_urls;
    }

    public String getRe_keys() {
        return re_keys;
    }

    public void setRe_keys(String re_keys) {
        this.re_keys = re_keys;
    }

    public String getRe_prime_app_minimum_android_version() {
        return re_prime_app_minimum_android_version;
    }

    public void setRe_prime_app_minimum_android_version(String re_prime_app_minimum_android_version) {
        this.re_prime_app_minimum_android_version = re_prime_app_minimum_android_version;
    }

    public String getRides_keys() {
        return rides_keys;
    }

    public void setRides_keys(String rides_keys) {
        this.rides_keys = rides_keys;
    }

    public String getNavigation_keys() {
        return navigation_keys;
    }

    public void setNavigation_keys(String navigation_keys) {
        this.navigation_keys = navigation_keys;
    }

    public String getServiceCentersLWPURL() {
        return serviceCentersLWPURL;
    }

    public void setServiceCentersLWPURL(String serviceCentersLWPURL) {
        this.serviceCentersLWPURL = serviceCentersLWPURL;
    }

    public String getServicebookingCutOffTime() {
        return servicebookingCutOffTime;
    }

    public void setServicebookingCutOffTime(String servicebookingCutOffTime) {
        this.servicebookingCutOffTime = servicebookingCutOffTime;
    }

    public String getTwoWheelerCountries() {
        return twoWheelerCountries;
    }

    public void setTwoWheelerCountries(String twoWheelerCountries) {
        this.twoWheelerCountries = twoWheelerCountries;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGlobal_validations() {
        return global_validations;
    }

    public void setGlobal_validations(String global_validations) {
        this.global_validations = global_validations;
    }

    public String getFeature_status() {
        return feature_status;
    }

    public void setFeature_status(String feature_status) {
        this.feature_status = feature_status;
    }
}