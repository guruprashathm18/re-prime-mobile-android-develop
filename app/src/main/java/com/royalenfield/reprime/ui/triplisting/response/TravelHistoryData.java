package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelHistoryData {

    @SerializedName("getTripDetails")
    @Expose
    private TravelHistory data;

    public TravelHistory getData() {
        return data;
    }
}
