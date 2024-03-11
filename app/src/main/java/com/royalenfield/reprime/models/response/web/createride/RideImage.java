package com.royalenfield.reprime.models.response.web.createride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideImage {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("srcPath")
    @Expose
    private String srcPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

}