package com.royalenfield.reprime.models.response.remoteconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSettings {
    @SerializedName("distanceUnit")
    @Expose
    private String distanceUnit;
    @SerializedName("decimalSeperator")
    @Expose
    private String decimalSeperator;

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getDecimalSeperator() {
        return decimalSeperator;
    }

    public void setDecimalSeperator(String decimalSeperator) {
        this.decimalSeperator = decimalSeperator;
    }
}
