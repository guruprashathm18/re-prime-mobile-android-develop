package com.royalenfield.reprime.models.response.googlemap.directions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.response.googlemap.PolyLine;

public class Step {

    @SerializedName("end_location")
    @Expose
    private EndLocation endLocation;

    @SerializedName("polyline")
    @Expose
    private PolyLine polyline;

    public PolyLine getPolyline() {
        return polyline;
    }

    public void setPolyline(PolyLine polyline) {
        this.polyline = polyline;
    }

    public EndLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }


}
