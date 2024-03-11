package com.royalenfield.reprime.ui.home.service.asynctask;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.application.REApplication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.royalenfield.reprime.utils.RELog;

/**
 * Gets the Location from the address provided
 */
public class GetLocationFromAddressAsyncTask extends AsyncTask<Void, Void, Void> {

    private String mOldAddress;
    private LatLng latLngOfUserEnteredAddress;
    private ServiceAsyncTaskListeners.onLocationComplete mListener;

    public GetLocationFromAddressAsyncTask(String address,
                                           ServiceAsyncTaskListeners.onLocationComplete listener) {
        mOldAddress = address;
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getLocationFromTheUserAddress(mOldAddress);
        return null;
    }

    private void getLocationFromTheUserAddress(final String oldAddress) {
        //Address address = REUtils.setLocationFromUserEnteredAddress( this, oldAddress );
        List<Address> addresses = null;
        Address address;
        Geocoder geocoder = new Geocoder(REApplication.getAppContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(oldAddress, 1);
        } catch (IOException e) {
            RELog.e(e);
        }
        if (addresses != null && addresses.size() > 0) {
            address = addresses.get(0);
            double longitude = address.getLongitude();
            double latitude = address.getLatitude();
            latLngOfUserEnteredAddress = new LatLng(latitude, longitude);

        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mListener != null) {
            mListener.onAddressFromLocationComplete(latLngOfUserEnteredAddress);
        }
    }
}
