package com.royalenfield.reprime.ui.home.navigation.asynctask;

import com.bosch.softtec.micro.tenzing.client.model.InlineResponse200;
import com.bosch.softtec.micro.tenzing.client.model.Trip;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

/**
 * Listeners for Navigation AsyncTask
 */
public class NavigationAsyncTaskListeners {

    public interface UploadTripListener {
        void onUploadTripSuccess(String result, boolean isSave, int totalSize);

        void onUploadTripFailure(String error, int totalSize);
    }

    public interface FetchTripListener {
        void onFetchTripSuccess(Trip trip);

        void onFetchTripFailure(String error);
    }
    public interface UpdateTripListener {
        void onUpdateTripSuccess(Boolean value);

        void onUpdateTripFailure();
    }
    public interface FetchAllTripsListener {
        void onFetchAllTripsSuccess(InlineResponse200 tripMetadataList);

        void onFetchAllTripsFailure(String error);
    }

    public interface LatLngListener {

        void onBCTLatLngSuccess(List<LatLng> latLngs, LatLngBounds.Builder builder);

        void onBCTLatLngFailure();
    }

    /*public interface FetchTripImagesListener {
        void onFetchTripImagesSuccess(List<Bitmap> bitmaps);

        void onFetchTripImagesFailure(String error);
    }*/
}
