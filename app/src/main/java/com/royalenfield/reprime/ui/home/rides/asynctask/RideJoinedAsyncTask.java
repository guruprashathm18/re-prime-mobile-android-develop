package com.royalenfield.reprime.ui.home.rides.asynctask;

import android.os.AsyncTask;

import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.UserUpcomingRidesResponse;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;
import java.util.Map;

import com.royalenfield.reprime.utils.RELog;

/**
 * AsyncTask for checking whether the user has joined the ride or not
 */
public class RideJoinedAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private int mPosition;
    private String mType;
    private List<DealerUpcomingRidesResponse> mDealerUpcomingRidesResponse;
    private List<UserUpcomingRidesResponse> mUserUpcomingRidesResponse;
    private RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener<List> mListener;

    public RideJoinedAsyncTask(int position, String type, List<DealerUpcomingRidesResponse> dealerUpcomingRidesResponses,
                               List<UserUpcomingRidesResponse> userUpcomingRidesResponses,
                               RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener<List> listener) {
        mPosition = position;
        mType = type;
        mDealerUpcomingRidesResponse = dealerUpcomingRidesResponses;
        mUserUpcomingRidesResponse = userUpcomingRidesResponses;
        mListener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            if (mType.equals(REConstants.TYPE_JOIN_DEALER_RIDE) && mDealerUpcomingRidesResponse != null &&
                    mDealerUpcomingRidesResponse.size() > 0) {
                for (int i = 0; i < mDealerUpcomingRidesResponse.get(mPosition).getRidersJoined().size(); i++) {
                    Map<String, Object> userObject = (Map<String, Object>) mDealerUpcomingRidesResponse.get(mPosition).
                            getRidersJoined().get(i).get(REFirestoreConstants.DEALER_RIDE_USER);
                    String userId = null;
                    if (userObject != null) {
                        userId = String.valueOf(userObject.get(REFirestoreConstants.RIDERS_JOINED_USER_ID));
                    }
                    if (userId != null && userId.equals(REUtils.getUserId())) {
                        return true;
                    }
                }
            } else if (mType.equals(REConstants.TYPE_USER_CREATED_RIDE_DETAILS) && mUserUpcomingRidesResponse != null &&
                    mUserUpcomingRidesResponse.size() > 0) {
                for (int i = 0; i < mUserUpcomingRidesResponse.get(mPosition).getRidersJoined().size(); i++) {
                    String userId = (String) mUserUpcomingRidesResponse.get(mPosition).getRidersJoined().get(i).getUser().
                            get(REFirestoreConstants.RIDERS_JOINED_USER_ID);
                    if (userId != null && userId.equals(REUtils.getUserId())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (ClassCastException e) {
            RELog.e(e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isJoined) {
        if (isJoined) {
            mListener.onRideJoinedComplete();
        }
    }
}
