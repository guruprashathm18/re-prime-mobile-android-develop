package com.royalenfield.reprime.models.response.proxy.deletepayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeletePaymentResponse {

    @SerializedName("statusCode")
    @Expose
    private String statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
