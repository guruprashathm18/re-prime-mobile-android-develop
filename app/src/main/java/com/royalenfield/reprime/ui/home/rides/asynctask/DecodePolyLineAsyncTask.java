package com.royalenfield.reprime.ui.home.rides.asynctask;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * DecodePolyLineAsyncTask is to decode the polyline fetched from Google Directions API
 */
public class DecodePolyLineAsyncTask extends AsyncTask<Void, Void, List<LatLng>> {

    private String mPolyline;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private RidesAsyncTaskListeners.DirectionPolylineDecodeListener mListener;

    public DecodePolyLineAsyncTask(String polyline, RidesAsyncTaskListeners.DirectionPolylineDecodeListener listener) {
        mPolyline = polyline;
        mListener = listener;
    }

    @Override
    protected List<LatLng> doInBackground(Void... voids) {
        return decodePolyLine(mPolyline);
    }

    /**
     * Decodes an encoded path string into a sequence of LatLngs.
     */
    private List<LatLng> decodePolyLine(String encodedPoint) {

        int len = encodedPoint.length();

        final List<LatLng> path = new ArrayList<>(len / 2);
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPoint.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPoint.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            LatLng latLng = new com.google.android.gms.maps.model.LatLng(lat * 1e-5, lng * 1e-5);
            path.add(latLng);
            builder.include(latLng);
        }

        return path;
    }

    @Override
    protected void onPostExecute(List<LatLng> latLngs) {
        mListener.onDecodeCompleted(latLngs, builder);
    }
}
