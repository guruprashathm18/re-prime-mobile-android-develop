package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TripDeleteResponseModel implements Serializable {
    @SerializedName("data")
    @Expose
    private TripDeleteData data;

    public TripDeleteData getData() {
        return data;
    }

    public void setData(TripDeleteData data) {
        this.data = data;
    }
}
