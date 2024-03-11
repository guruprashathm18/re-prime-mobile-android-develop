
package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileappbaseURLs {

    @SerializedName("appBaseURL")
    @Expose
    private String appBaseURL;
    @SerializedName("assetsURL")
    @Expose
    private String assetsURL;
    @SerializedName("bctURL")
    @Expose
    private String bctURL;
    @SerializedName("configuratorServicesURL")
    @Expose
    private String configuratorServicesURL;
    @SerializedName("deviceTokenServicesURL")
    @Expose
    private String deviceTokenServicesURL;
    @SerializedName("generatePaymentMessageURL")
    @Expose
    private String generatepaymentmessageurl;
    @SerializedName("lightweightPageURL")
    @Expose
    private String lightweightPageURL;
    @SerializedName("logsUploadURL")
    @Expose
    private String logsUploadURL;
    @SerializedName("paymentURL")
    @Expose
    private String paymentURL;
    @SerializedName("tbtURL")
    @Expose
    private String tbtURL;
    @SerializedName("configurationShareUrl")
    @Expose
    private String configurationShareUrl;

    @SerializedName("MIY_URL")
    @Expose
    private String miyURL;

    @SerializedName("DRSA_URL")
    @Expose
    private String drsaUrl;

    @SerializedName("Wanderlust_URL")
    @Expose
    private String wanderLustUrl;

    @SerializedName("Wanderlust_Trips_URL")
    @Expose
    private String tripsUrl;

    @SerializedName("ConnectedSocketURL")
    @Expose
    private String ConnectedSocketURL;

    @SerializedName("ServiceBookingURL")
    @Expose
    private String ServiceBookingURL;

    @SerializedName("orderTrackingBaseURL")
    @Expose
    private String orderTrackingBaseURL;

    @SerializedName("financeYourRE")
    @Expose
    private String financeYourRE;

	@SerializedName("appBaseURL_V4")
	@Expose
	private String appBaseURLV4;


	public String getFinanceYourRE() {
        return financeYourRE;
    }

    public void setFinanceYourRE(String financeYourRE) {
        this.financeYourRE = financeYourRE;
    }

    public String getOrderTrackingBaseURL() {
        return orderTrackingBaseURL;
    }

    public void setOrderTrackingBaseURL(String orderTrackingBaseURL) {
        this.orderTrackingBaseURL = orderTrackingBaseURL;
    }

    @SerializedName("firebaseTokenBaseURL")
    @Expose
    private String firebaseTokenBaseURL;

    @SerializedName("ourWorldWeb")
    @Expose
    private String ourWorldWeb;
    @SerializedName("ridesWeb")
    @Expose
    private String ridesWeb;
    @SerializedName("countryUrl")
    @Expose
    private String countryUrl;

	@SerializedName("connected_logs_url")
	@Expose
	private String connectedLogsUrl;

    public String getCountryUrl() {
        return countryUrl;
    }

    public void setCountryUrl(String countryUrl) {
        this.countryUrl = countryUrl;
    }

    public String getOurWorldWeb() {
        return ourWorldWeb;
    }

    public void setOurWorldWeb(String ourWorldWeb) {
        this.ourWorldWeb = ourWorldWeb;
    }

    public String getRidesWeb() {
        return ridesWeb;
    }

    public void setRidesWeb(String ridesWeb) {
        this.ridesWeb = ridesWeb;
    }

    public String getFirebaseTokenBaseURL() {
        return firebaseTokenBaseURL;
    }

    public void setFirebaseTokenBaseURL(String firebaseTokenBaseURL) {
        this.firebaseTokenBaseURL = firebaseTokenBaseURL;
    }

    public String getServiceBookingURL() {
        return ServiceBookingURL;
    }

    public void setServiceBookingURL(String serviceBookingURL) {
        ServiceBookingURL = serviceBookingURL;
    }

    @SerializedName("ConnectedBaseURL")
    @Expose
    private String ConnectedBaseURL;

    public String getConnectedBaseURL() {
        return ConnectedBaseURL;
    }

    public void setConnectedBaseURL(String connectedBaseURL) {
        ConnectedBaseURL = connectedBaseURL;
    }

    public String getConnectedSocketURL() {
        return ConnectedSocketURL;
    }

    public void setConnectedSocketURL(String connectedSocketURL) {
        ConnectedSocketURL = connectedSocketURL;
    }

    @SerializedName("riderPortalURL")
    @Expose
    private String riderPortalURL;


    public String getDrsaUrl() {
        return drsaUrl;
    }

    public void setDrsaUrl(String drsaUrl) {
        this.drsaUrl = drsaUrl;
    }

    public String getMiyURL() {
        return miyURL;
    }

    public void setMiyURL(String miyURL) {
        this.miyURL = miyURL;
    }

    public String getAppBaseURL() {
        return appBaseURL;
    }

    public void setAppBaseURL(String appBaseURL) {
        this.appBaseURL = appBaseURL;
    }

    public String getAssetsURL() {
        return assetsURL;
    }

    public void setAssetsURL(String assetsURL) {
        this.assetsURL = assetsURL;
    }

    public String getBctURL() {
        return bctURL;
    }

    public void setBctURL(String bctURL) {
        this.bctURL = bctURL;
    }

    public String getConfiguratorServicesURL() {
        return configuratorServicesURL;
    }

    public void setConfiguratorServicesURL(String configuratorServicesURL) {
        this.configuratorServicesURL = configuratorServicesURL;
    }

    public String getDeviceTokenServicesURL() {
        return deviceTokenServicesURL;
    }

    public void setDeviceTokenServicesURL(String deviceTokenServicesURL) {
        this.deviceTokenServicesURL = deviceTokenServicesURL;
    }

    public String getGeneratepaymentmessageurl() {
        return generatepaymentmessageurl;
    }

    public void setGeneratepaymentmessageurl(String generatepaymentmessageurl) {
        this.generatepaymentmessageurl = generatepaymentmessageurl;
    }

    public String getLightweightPageURL() {
        return lightweightPageURL;
    }

    public void setLightweightPageURL(String lightweightPageURL) {
        this.lightweightPageURL = lightweightPageURL;
    }

    public String getLogsUploadURL() {
        return logsUploadURL;
    }

    public void setLogsUploadURL(String logsUploadURL) {
        this.logsUploadURL = logsUploadURL;
    }

    public String getPaymentURL() {
        return paymentURL;
    }

    public void setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
    }

    public String getTbtURL() {
        return tbtURL;
    }

    public void setTbtURL(String tbtURL) {
        this.tbtURL = tbtURL;
    }

    public String getConfigurationShareUrl() {
        return configurationShareUrl;
    }

    public void setConfigurationShareUrl(String configurationShareUrl) {
        this.configurationShareUrl = configurationShareUrl;
    }

    public String getRiderPortalURL() {
        return riderPortalURL;
    }

    public void setRiderPortalURL(String riderPortalURL) {
        this.riderPortalURL = riderPortalURL;
    }

    public String getWanderLustUrl() {
        return wanderLustUrl;
    }

    public void setWanderLustUrl(String wanderLustUrl) {
        this.wanderLustUrl = wanderLustUrl;
    }

    public String getTripsUrl() {
        return tripsUrl;
    }

    public void setTripsUrl(String tripsUrl) {
        this.tripsUrl = tripsUrl;
    }

	public String getAppBaseURLV4() {
		return appBaseURLV4;
	}

	public void setAppBaseURLV4(String appBaseURLV4) {
		this.appBaseURLV4 = appBaseURLV4;
	}

	public String getConnectedLogsUrl() {
		return connectedLogsUrl;
	}

	public void setConnectedLogsUrl(String connectedLogsUrl) {
		this.connectedLogsUrl = connectedLogsUrl;
	}
}
