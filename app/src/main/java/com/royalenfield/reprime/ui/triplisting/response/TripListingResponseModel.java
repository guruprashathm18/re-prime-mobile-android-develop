package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TripListingResponseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private TripData data;

    public TripData getData() {
        return data;
    }

    public void setData(TripData data) {
        this.data = data;
    }
}
