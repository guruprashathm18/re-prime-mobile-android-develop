package com.royalenfield.reprime.models.response.googlemap.poi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("photo_reference")
    @Expose
    private String photoReference;


    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

}
