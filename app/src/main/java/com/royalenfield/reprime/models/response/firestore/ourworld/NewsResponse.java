package com.royalenfield.reprime.models.response.firestore.ourworld;

public class NewsResponse {

    private String description;

    private String newsDateString;

    private String newsTitle;

    private String thumbnailImageSrc;

    private String contentBody;

    private String lightWeightPageUrl;

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewsDateString() {
        return newsDateString;
    }

    public void setNewsDateString(String newsDateString) {
        this.newsDateString = newsDateString;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }


    public String getThumbnailImageSrc() {
        return thumbnailImageSrc;
    }

    public void setThumbnailImageSrc(String thumbnailImageSrc) {
        this.thumbnailImageSrc = thumbnailImageSrc;
    }


    public String getLightWeightPageUrl() {
        return lightWeightPageUrl;
    }

    public void setLightWeightPageUrl(String lightWeightPageUrl) {
        this.lightWeightPageUrl = lightWeightPageUrl;
    }
}
