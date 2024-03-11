package com.royalenfield.reprime.ui.riderprofile.presenter;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.ui.home.rides.views.IRidesTourRowView;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_ONGOING;
import static com.royalenfield.reprime.utils.REConstants.UPCOMIMGRIDE_TYPE;

public class RiderInvitedPresenter implements IRiderInvitedPresenter {

    private List<ProfileRidesResponse> mOngoingRideResponse = REUserModelStore.getInstance().getOngoingRideResponse();
    private List<ProfileRidesResponse> mUpcomingRideResponse = REUserModelStore.getInstance().getUpcomingRideResponse();
    private String baseUrl = REUtils.getMobileappbaseURLs().getAssetsURL();

    @Override
    public void onRiderInvitedRowViewAtPosition(int position, IRidesTourRowView rowView, int mListPosition, String rideType) {
        try {
            if (rideType.equals(RIDE_TYPE_ONGOING)) {
                rowView.setKeyPlaceImage(baseUrl + mOngoingRideResponse.get(mListPosition).
                        getRidersJoined().get(position).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_PROFILE_URL));
                rowView.setKeyPlaceName(mOngoingRideResponse.get(mListPosition).
                        getRidersJoined().get(position).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_FNAME) +
                        " "+mOngoingRideResponse.get(mListPosition).
                        getRidersJoined().get(position).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_LNAME));
            } else if (rideType.equals(UPCOMIMGRIDE_TYPE)) {
                rowView.setKeyPlaceImage(baseUrl + mUpcomingRideResponse.get(mListPosition).
                        getRidersJoined().get(position).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_PROFILE_URL));
                rowView.setKeyPlaceName(mUpcomingRideResponse.get(mListPosition).
                        getRidersJoined().get(position).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_FNAME) +
                        " "+mUpcomingRideResponse.get(mListPosition).
                        getRidersJoined().get(position).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_LNAME));
            }
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    public int getRideTourRowsCount(int count) {
        return count;
    }
}
