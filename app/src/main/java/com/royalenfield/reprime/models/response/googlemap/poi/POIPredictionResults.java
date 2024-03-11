package com.royalenfield.reprime.models.response.googlemap.poi;

import com.bosch.softtec.components.midgard.core.search.models.results.SearchResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author kiran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class POIPredictionResults {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("distance_meters")
    @Expose
    private double distanceMeters;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("structured_formatting")
    @Expose
    private StructuredFormatting structuredFormatting = null;
    private SearchResult searchResults;

    public SearchResult getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(SearchResult searchResults) {
        this.searchResults = searchResults;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public StructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    public void setStructuredFormatting(StructuredFormatting structuredFormatting) {
        this.structuredFormatting = structuredFormatting;
    }
}
