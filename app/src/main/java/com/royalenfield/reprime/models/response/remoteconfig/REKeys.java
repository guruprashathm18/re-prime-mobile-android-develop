
package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class REKeys {

    @SerializedName("CustomerCare")
    @Expose
    private String customerCare;
    @SerializedName("CustomerSupport")
    @Expose
    private String customerSupport;

    @SerializedName("JWTExpiryThreshold")
    @Expose
    private String jWTExpiryThreshold;
    @SerializedName("JWTRetryAttemptsCount")
    @Expose
    private String jWTRetryAttemptsCount;
    @SerializedName("enableLogging")
    @Expose
    private Boolean enableLogging;

    public String getCustomerCare() {
        return customerCare;
    }

    public void setCustomerCare(String customerCare) {
        this.customerCare = customerCare;
    }

    public String getJWTExpiryThreshold() {
        return jWTExpiryThreshold;
    }

    public void setJWTExpiryThreshold(String jWTExpiryThreshold) {
        this.jWTExpiryThreshold = jWTExpiryThreshold;
    }

    public String getJWTRetryAttemptsCount() {
        return jWTRetryAttemptsCount;
    }

    public void setJWTRetryAttemptsCount(String jWTRetryAttemptsCount) {
        this.jWTRetryAttemptsCount = jWTRetryAttemptsCount;
    }

    public Boolean getEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(Boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public String getCustomerSupport() {
        return customerSupport;
    }

    public void setCustomerSupport(String customerSupport) {
        this.customerSupport = customerSupport;
    }
}
