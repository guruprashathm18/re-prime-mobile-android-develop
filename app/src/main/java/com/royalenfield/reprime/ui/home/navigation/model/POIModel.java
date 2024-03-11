package com.royalenfield.reprime.ui.home.navigation.model;

import com.bosch.softtec.components.midgard.core.search.models.PoiCategory;

public class POIModel {

    private String name;
    private int drawable;
    private boolean selected;
    private PoiCategory poiCategory;

    public POIModel(String name, int drawable, PoiCategory poiCategory) {
        this.name = name;
        this.drawable = drawable;
        this.poiCategory = poiCategory;
        selected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public PoiCategory getPoiCategory() {
        return poiCategory;
    }

    public void setPoiCategory(PoiCategory poiCategory) {
        this.poiCategory = poiCategory;
    }
}