
package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class REGoogleKeys {

    @SerializedName("APIKey")
    @Expose
    private String kRideServiceAPIKey;
    @SerializedName("APIKeyAndroid")
    @Expose
    private String aPIKey;
    @SerializedName("YoutubeAPIKey")
    @Expose
    private String youtubeaPIKey;
    @SerializedName("googleMapAppStoreLink")
    @Expose
    private String googleMapAppStoreLink;
    @SerializedName("googleURLScheme")
    @Expose
    private String googleURLScheme;
    @SerializedName("staticMapKey")
    @Expose
    private String staticMapKey;

    public String getAPIKey() {
        return aPIKey;
    }

    public void setAPIKey(String aPIKey) {
        this.aPIKey = aPIKey;
    }

    public String getYoutubeaPIKey() {
        return youtubeaPIKey;
    }

    public void setYoutubeaPIKey(String youtubeaPIKey) {
        this.youtubeaPIKey = youtubeaPIKey;
    }

    public String getGoogleMapAppStoreLink() {
        return googleMapAppStoreLink;
    }

    public void setGoogleMapAppStoreLink(String googleMapAppStoreLink) {
        this.googleMapAppStoreLink = googleMapAppStoreLink;
    }

    public String getGoogleURLScheme() {
        return googleURLScheme;
    }

    public void setGoogleURLScheme(String googleURLScheme) {
        this.googleURLScheme = googleURLScheme;
    }

    public String getStaticMapKey() {
        return staticMapKey;
    }

    public void setStaticMapKey(String staticMapKey) {
        this.staticMapKey = staticMapKey;
    }

    public String getRideServiceAPIKey() {
        return kRideServiceAPIKey;
    }

    public void setRideServiceAPIKey(String kRideServiceAPIKey) {
        this.kRideServiceAPIKey = kRideServiceAPIKey;
    }
}
