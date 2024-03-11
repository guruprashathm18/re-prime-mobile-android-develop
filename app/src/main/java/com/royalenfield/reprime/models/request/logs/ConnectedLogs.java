package com.royalenfield.reprime.models.request.logs;

		import com.google.gson.annotations.Expose;
		import com.google.gson.annotations.SerializedName;

public class ConnectedLogs {

	@SerializedName("timeStamp")
	@Expose
	private String timeStamp;
	@SerializedName("environment")
	@Expose
	private String environment;
	@SerializedName("vin")
	@Expose
	private String vin;
	@SerializedName("imei")
	@Expose
	private String imei;
	@SerializedName("apiName")
	@Expose
	private String apiName;
	@SerializedName("errorMessage")
	@Expose
	private String errorMessage;
	@SerializedName("request")
	@Expose
	private ConnectedRequest request;
	@SerializedName("response")


	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ConnectedRequest getRequest() {
		return request;
	}

	public void setRequest(ConnectedRequest request) {
		this.request = request;
	}
}