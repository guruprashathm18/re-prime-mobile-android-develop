package com.royalenfield.reprime.ui.home.navigation.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.bosch.softtec.components.nephele.CloudException;
import com.bosch.softtec.micro.tenzing.client.model.ShareTicketCode;
import com.bosch.softtec.micro.tenzing.client.model.Trip;
import com.royalenfield.reprime.application.REApplication;

/**
 * Async task for creating tripId for BCTn
 */
public class FetchTripAsyncTask extends AsyncTask<String, String, Trip> {

    private CloudException exception;
    private String strAction;
    private NavigationAsyncTaskListeners.FetchTripListener mListener;

    public FetchTripAsyncTask(String action, NavigationAsyncTaskListeners.FetchTripListener listener) {
        strAction = action;
        mListener = listener;
    }
    @Override
    protected Trip doInBackground(String... params) {
        try {
            String strSharedTripId = params[0];
            if(strAction.equalsIgnoreCase("share")) {
                ShareTicketCode shareTicketCode = new ShareTicketCode();
                shareTicketCode.code(params[0]);
                strSharedTripId = REApplication.getInstance().getCloudManager().redeemSharedTrip(shareTicketCode);
            }
            return REApplication.getInstance().getCloudManager().fetchTripById(strSharedTripId);
        }catch (CloudException e){
            exception = e;
            Log.e("test", "exception raised  = "+exception.getHttpErrorMessage());
            return null;
        }
    }


    @Override
    protected void onPostExecute(Trip result) {
        // execution of result of Long time consuming operation
        if(result != null && result.getTripId() != null && result.getTripId().length() > 0 ) {
            Log.e("test", "Trip found = " + result.getTripId());
            mListener.onFetchTripSuccess(result);
        }else{
            mListener.onFetchTripFailure(exception.getHttpErrorMessage());
        }
    }


    @Override
    protected void onPreExecute() {
    }
}
