package com.royalenfield.reprime.models.response.googlemap.poi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author kiran
 */
public class AutoCompleteSearchResponse {

    @SerializedName("predictions")
    @Expose
    private List<POIPredictionResults> predictions = null;

    public List<POIPredictionResults> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<POIPredictionResults> results) {
        this.predictions = results;
    }

}
