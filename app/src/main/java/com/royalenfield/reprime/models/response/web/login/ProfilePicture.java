package com.royalenfield.reprime.models.response.web.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfilePicture implements Serializable {

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
