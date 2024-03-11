package com.royalenfield.reprime.ui.triplisting.response;


import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class TripDeleteData {

    @SerializedName("tripDelete")
    @Expose
    private TripDelete tripDelete;

    public TripDelete getTripDelete() {
        return tripDelete;
    }

    public void setTripDelete(TripDelete tripDelete) {
        this.tripDelete = tripDelete;
    }

}