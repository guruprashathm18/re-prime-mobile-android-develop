package com.royalenfield.reprime.ui.home.navigation.asynctask;

import android.os.AsyncTask;

import com.bosch.softtec.components.nephele.CloudException;
import com.bosch.softtec.micro.tenzing.client.model.InlineResponse200;
import com.royalenfield.extensions.BCTUploadExtensionsKt;

/**
 * Async task for creating tripId for BCT
 */
public class FetchAllCloudTripsAsyncTask extends AsyncTask<String, String, InlineResponse200> {

    private CloudException exception;
    private NavigationAsyncTaskListeners.FetchAllTripsListener mListener;

    public FetchAllCloudTripsAsyncTask(NavigationAsyncTaskListeners.FetchAllTripsListener listener) {
        mListener = listener;
    }
    @Override
    protected InlineResponse200 doInBackground(String... params) {
        InlineResponse200 data = null;
        try {
             data = BCTUploadExtensionsKt.fetchAllCloudTrips(params[0]);
        }catch (CloudException e){
            exception = e;
        }
        return data;
    }


    @Override
    protected void onPostExecute(InlineResponse200 result) {
        // execution of result of Long time consuming operation
        if(result != null) {
            mListener.onFetchAllTripsSuccess(result);
        }else{
            mListener.onFetchAllTripsFailure(exception.getHttpErrorMessage());
        }
    }
}
