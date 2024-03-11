package com.royalenfield.reprime.models.response.web.navigation;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLocation {

    @SerializedName("results")
    @Expose
    private List<LocationResult> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<LocationResult> getResults() {
        return results;
    }

    public void setResults(List<LocationResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
