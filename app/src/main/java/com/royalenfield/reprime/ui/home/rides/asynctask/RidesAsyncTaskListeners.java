package com.royalenfield.reprime.ui.home.rides.asynctask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

public class RidesAsyncTaskListeners {
    public interface RidesAsyncTaskCompleteListener<T> {
        void onRidesDistanceCalcComplete(T result);

        void onRideJoinedComplete();
    }

    public interface DirectionPolylineDecodeListener {
        void onDecodeCompleted(List<LatLng> latLngList, LatLngBounds.Builder builder);
    }
}
