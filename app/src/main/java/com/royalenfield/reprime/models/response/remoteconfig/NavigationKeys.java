package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NavigationKeys {
    @SerializedName("trail_record_limit")
    @Expose
    private String trailRecordLimit;

    public String getTrailRecordLimit() {
        return trailRecordLimit;
    }

    public void setTrailRecordLimit(String trailRecordLimit) {
        this.trailRecordLimit = trailRecordLimit;
    }
}
