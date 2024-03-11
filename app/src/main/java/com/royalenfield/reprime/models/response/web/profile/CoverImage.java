package com.royalenfield.reprime.models.response.web.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoverImage {

    @SerializedName("srcPath")
    @Expose
    private String srcPath;

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

}
