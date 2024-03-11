package com.royalenfield.reprime.ui.home.navigation.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.bosch.softtec.components.nephele.CloudException;
import com.bosch.softtec.micro.tenzing.client.model.JsonPatch;
import com.royalenfield.reprime.application.REApplication;

import java.util.List;

import timber.log.Timber;

/**
 * Async task for creating tripId for BCT
 */
public class EditTripAsyncTask extends AsyncTask<Boolean, Void, Boolean> {

    private List<JsonPatch> jsonPatches;
    private CloudException exception;
    private NavigationAsyncTaskListeners.UpdateTripListener mListener;
    private String tripID;


    public EditTripAsyncTask(NavigationAsyncTaskListeners.UpdateTripListener listener,String mTripID, List<JsonPatch> mjsonPatches) {
        mListener = listener;
        jsonPatches = mjsonPatches;
        tripID = mTripID;
    }

    @Override
    protected Boolean doInBackground(Boolean... params) {
        Boolean Success = false;

        try {
            Success = updateTripDetails();

        } catch (CloudException e) {
            exception = e;
            Timber.e(e);
            Log.e("Uploadtrip", " :" + e.getHttpErrorMessage());
            return false;
        }
        return Success;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (exception == null) {
            if (result != null && !result.equals("") ) {
                if (result) {
                    mListener.onUpdateTripSuccess(result);
                } else {
                    mListener.onUpdateTripFailure();
                }
            } else {
                mListener.onUpdateTripFailure();
            }
        } else {
            mListener.onUpdateTripFailure();
        }
    }

    private Boolean updateTripDetails() {
        Boolean isUpdateSuccess = REApplication.getInstance().getCloudManager().updateTrip(tripID,jsonPatches);
        return isUpdateSuccess;
    }
}
