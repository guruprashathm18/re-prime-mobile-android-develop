package com.royalenfield.reprime.models.response.web.ourworldnews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OurWorldNewsEntity {

    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("coverImage")
    @Expose
    private String coverImage;
    @SerializedName("thumbnailImage")
    @Expose
    private String thumbnailImage;
    @SerializedName("url")
    @Expose
    private String url;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
