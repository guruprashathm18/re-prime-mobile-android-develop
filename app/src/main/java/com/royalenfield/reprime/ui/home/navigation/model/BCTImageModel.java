package com.royalenfield.reprime.ui.home.navigation.model;

import android.graphics.Bitmap;

import com.bosch.softtec.components.midgard.core.search.models.PoiCategory;

public class BCTImageModel {

    private String name;
    private Bitmap drawable;
    private boolean selected;
    private PoiCategory poiCategory;

    public BCTImageModel(String name, Bitmap drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getDrawable() {
        return drawable;
    }

    public void setDrawable(Bitmap drawable) {
        this.drawable = drawable;
    }
}