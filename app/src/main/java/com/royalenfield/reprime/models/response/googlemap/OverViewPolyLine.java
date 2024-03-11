package com.royalenfield.reprime.models.response.googlemap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverViewPolyLine {

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @SerializedName("points")
    @Expose
    private String points = null;
}
