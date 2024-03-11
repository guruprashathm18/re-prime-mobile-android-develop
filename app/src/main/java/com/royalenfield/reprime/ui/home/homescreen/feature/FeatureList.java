package com.royalenfield.reprime.ui.home.homescreen.feature;

import java.io.Serializable;

public class FeatureList implements Serializable {
    private boolean isFeatureEnabled;
    private String featureString;

    public FeatureList(boolean  selected, String name) {
        this.isFeatureEnabled = selected;
        this.featureString = name;
    }

    public String getFeatureString() {
        return featureString;
    }

    public void setFeatureString(String featureString) {
        this.featureString = featureString;
    }

    public boolean isFeatureEnabled() {
        return isFeatureEnabled;
    }

    public void setFeatureEnabled(boolean selected) {
        isFeatureEnabled = selected;
    }
}
