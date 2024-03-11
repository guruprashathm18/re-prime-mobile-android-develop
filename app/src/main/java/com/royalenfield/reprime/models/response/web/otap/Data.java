package com.royalenfield.reprime.models.response.web.otap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {
    @SerializedName("active")
    @Expose
    private ArrayList<DeviceInfoApiItems> active;
    @SerializedName("bricked")
    @Expose
    private ArrayList<DeviceInfoApiItems> bricked;

    public ArrayList<DeviceInfoApiItems> getActive() {
        return active;
    }

    public void setActive(ArrayList<DeviceInfoApiItems> active) {
        this.active = active;
    }

    public ArrayList<DeviceInfoApiItems> getBricked() {
        return bricked;
    }

    public void setBricked(ArrayList<DeviceInfoApiItems> bricked) {
        this.bricked = bricked;
    }
}
