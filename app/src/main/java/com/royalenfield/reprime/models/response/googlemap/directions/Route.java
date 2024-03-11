package com.royalenfield.reprime.models.response.googlemap.directions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.response.googlemap.OverViewPolyLine;

import java.util.List;

public class Route {

    @SerializedName("legs")
    @Expose
    private List<Leg> legs = null;

    public OverViewPolyLine getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(OverViewPolyLine overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    @SerializedName("overview_polyline")
    @Expose
    private OverViewPolyLine overview_polyline = null;

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }


}
