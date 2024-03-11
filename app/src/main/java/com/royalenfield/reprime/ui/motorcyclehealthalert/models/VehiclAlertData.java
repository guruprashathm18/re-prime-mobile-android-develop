package com.royalenfield.reprime.ui.motorcyclehealthalert.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclAlertData {
    @SerializedName("getAllAlertsByPagination")
    @Expose
    private List<GetAllAlertsByUniqueId> getAllAlertsByUniqueId = null;

    public List<GetAllAlertsByUniqueId> getGetAllAlertsByUniqueId() {
        return getAllAlertsByUniqueId;
    }

    public void setGetAllAlertsByUniqueId(List<GetAllAlertsByUniqueId> getAllAlertsByUniqueId) {
        this.getAllAlertsByUniqueId = getAllAlertsByUniqueId;
    }
}
