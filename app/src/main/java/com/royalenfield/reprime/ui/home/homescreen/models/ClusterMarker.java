package com.royalenfield.reprime.ui.home.homescreen.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * @Author Kiran Dani on 14-Dec-2020
 */
public class ClusterMarker implements ClusterItem {
    private final LatLng position;

    public ClusterMarker(LatLng latLng) {
        position = latLng;
    }

    /**
     * The position of this marker. This must always return the same value.
     */
    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    /**
     * The title of this marker.
     */
    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * The description of this marker.
     */
    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }
}
