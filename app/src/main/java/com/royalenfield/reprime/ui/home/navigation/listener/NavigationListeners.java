package com.royalenfield.reprime.ui.home.navigation.listener;

import com.royalenfield.reprime.models.response.googlemap.poi.POIPredictionResults;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;

import java.util.List;

/**
 * @author kiran
 */
public class NavigationListeners {


    public interface GooglePOIListener {
        void getPlaces(String query, String aLocation, OnPlacesCallFinishedListener listener);

        void getPlaceDetails(String placeId, OnPlaceDetailsCallFinishedListener listener);

        void getPlaceName(String aLocation, OnPlaceFinishedListener listener);
    }

    public interface OnPlacesCallFinishedListener {
        void onPlacesSuccess(List<POIPredictionResults> autoCompleteSearchResponse);

        void onPlacesFailure(String error);
    }

    public interface OnPlaceDetailsCallFinishedListener {
        void onPlaceDetailsSuccess(POIResults poiResult);

        void onPlaceDetailsFailure(String error);
    }

    public interface OnPlaceFinishedListener {
        void onPlaceSuccess(String formattedAddress);

        void onPlaceFailure(String error);
    }
}
