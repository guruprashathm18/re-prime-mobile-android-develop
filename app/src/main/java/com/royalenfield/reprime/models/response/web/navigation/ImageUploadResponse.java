package com.royalenfield.reprime.models.response.web.navigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUploadResponse {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ImageUploadData data;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ImageUploadData getData() {
        return data;
    }

    public void setData(ImageUploadData data) {
        this.data = data;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

}