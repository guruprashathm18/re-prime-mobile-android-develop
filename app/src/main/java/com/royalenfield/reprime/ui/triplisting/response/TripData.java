package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TripData implements Serializable {

    @SerializedName("getTripsPagination")
    @Expose
    private List<GetActivitySummary> getActivitySummary = null;
    private final static long serialVersionUID = 8505120027120331392L;

    public List<GetActivitySummary> getGetActivitySummary() {
        return getActivitySummary;
    }

    public void setGetActivitySummary(List<GetActivitySummary> getActivitySummary) {
        this.getActivitySummary = getActivitySummary;
    }


}
