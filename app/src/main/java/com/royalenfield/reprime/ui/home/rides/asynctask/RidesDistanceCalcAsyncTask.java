package com.royalenfield.reprime.ui.home.rides.asynctask;

import android.os.AsyncTask;

import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.service.search.adapter.DistanceCalculationHelper;
import com.royalenfield.reprime.utils.REFirestoreConstants;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;


/**
 * Distance calculation Async Task for rides
 */
public class RidesDistanceCalcAsyncTask extends AsyncTask<Void, Void, List> {

    private String mType;
    public RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener<List> mListener;

    public RidesDistanceCalcAsyncTask(String type, RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener<List> listener) {
        mType = type;
        mListener = listener;
    }


    @Override
    protected List doInBackground(Void... voids) {
        try {
            switch (mType) {
                case REFirestoreConstants.DEALER_UPCOMING_RIDES:
                    return DistanceCalculationHelper.getInstances().
                            getSortedDealerRidesByDistance(RERidesModelStore.getInstance().getDealerUpcomingRidesResponse());
                case REFirestoreConstants.MARQUEE_RIDES_FIRESTORE_KEY:
                    return DistanceCalculationHelper.getInstances().getSortedMarqueeRidesResponse
                            (RERidesModelStore.getInstance().getMarqueeRidesResponse());
                case REFirestoreConstants.USER_UPCOMING_RIDES:
                    return DistanceCalculationHelper.getInstances().getSortedUserUpcomingRides
                            (RERidesModelStore.getInstance().getUserUpcomingRidesResponse());
                case REFirestoreConstants.POPULAR_RIDES_FIRESTORE_KEY:
                    return DistanceCalculationHelper.getInstances().getSortedPopularRides
                            (RERidesModelStore.getInstance().getPopularRidesResponse());
                default:
                    break;
            }
            return null;
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
            return null;
        } catch (Exception e) {
            RELog.e(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List list) {
        mListener.onRidesDistanceCalcComplete(list);
    }
}
