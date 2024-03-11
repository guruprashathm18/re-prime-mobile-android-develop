
package com.royalenfield.reprime.models.response.web.vehicleonboarding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadVehicleResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

}
