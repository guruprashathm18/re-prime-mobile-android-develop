package com.royalenfield.reprime.models.response.googlemap.poi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearBySearchResponse {

    @SerializedName("results")
    @Expose
    private List<POIResults> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<POIResults> getResults() {
        return results;
    }

    public void setResults(List<POIResults> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
