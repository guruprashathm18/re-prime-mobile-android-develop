package com.royalenfield.reprime.models.response.web.navigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageUploadData {

    @SerializedName("uriList")
    @Expose
    private List<String> uriList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}