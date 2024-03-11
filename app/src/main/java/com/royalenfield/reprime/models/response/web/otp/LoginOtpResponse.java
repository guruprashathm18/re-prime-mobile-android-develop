package com.royalenfield.reprime.models.response.web.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginOtpResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("code")
    @Expose
    private String code;

	@SerializedName("request_id")
	@Expose
	private String requestId;

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
