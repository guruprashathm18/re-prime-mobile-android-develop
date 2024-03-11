package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelHistoryResponseModel {

    @SerializedName("data")
    @Expose
    private TravelHistoryData data;

    public TravelHistoryData getData() {
        return data;
    }
}
