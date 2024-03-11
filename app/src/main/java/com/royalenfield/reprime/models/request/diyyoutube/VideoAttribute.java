package com.royalenfield.reprime.models.request.diyyoutube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.request.diyyoutube.Item;

import java.util.List;

public class VideoAttribute {
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
