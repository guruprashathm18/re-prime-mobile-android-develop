
package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiyVideo {

    @SerializedName("videoName")
    @Expose
    private String videoName;
    @SerializedName("videoId")
    @Expose
    private String videoId;

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

}
