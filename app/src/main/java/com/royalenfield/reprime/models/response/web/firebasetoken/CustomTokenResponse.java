package com.royalenfield.reprime.models.response.web.firebasetoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomTokenResponse {

        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private String data;
        @SerializedName("success")
        @Expose
        private Boolean success;
	@SerializedName("request_id")
	@Expose
	private String requestId;


        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
