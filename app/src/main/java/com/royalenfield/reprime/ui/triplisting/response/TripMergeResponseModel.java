package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripMergeResponseModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }
}
