package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoggerBaseUrls {
    @SerializedName("baseURL")
    @Expose
    private String baseURL;
    @SerializedName("query_param_key")
    @Expose
    private String queryParamKey;
    @SerializedName("query_param_value")
    @Expose
    private String queryParamValue;

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getQueryParamKey() {
        return queryParamKey;
    }

    public void setQueryParamKey(String queryParamKey) {
        this.queryParamKey = queryParamKey;
    }

    public String getQueryParamValue() {
        return queryParamValue;
    }

    public void setQueryParamValue(String queryParamValue) {
        this.queryParamValue = queryParamValue;
    }
}
