package com.royalenfield.reprime.ui.home.navigation.asynctask;

import android.os.AsyncTask;

import com.bosch.softtec.components.core.models.breadcrumb.Breadcrumb;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Async Task for getting the LatLng List
 */
public class BCTLatLngAsyncTask extends AsyncTask<Void, Void, List<LatLng>> {

    private NavigationAsyncTaskListeners.LatLngListener mListener;
    private List<Breadcrumb> mBreadcrumb;
    private List<LatLng> path = new ArrayList<>();
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    public BCTLatLngAsyncTask(NavigationAsyncTaskListeners.LatLngListener listener, List<Breadcrumb> breadcrumbs) {
        mListener = listener;
        mBreadcrumb = breadcrumbs;
    }

    @Override
    protected List<LatLng> doInBackground(Void... voids) {
        LatLng latLng;
        if (mBreadcrumb != null && mBreadcrumb.size() > 0) {
            for (int iCount = 0; iCount < mBreadcrumb.size(); iCount++) {
                latLng = new com.google.android.gms.maps.model.LatLng(mBreadcrumb.get(iCount).
                        getLocation().getLatitude(), mBreadcrumb.get(iCount).
                        getLocation().getLongitude());
                path.add(latLng);
                builder.include(latLng);
            }
        }
        return path;
    }

    @Override
    protected void onPostExecute(List<LatLng> latLngs) {
        if (mListener != null) {
            if (latLngs != null)
                mListener.onBCTLatLngSuccess(latLngs, builder);
            else
                mListener.onBCTLatLngFailure();
        }
    }
}
