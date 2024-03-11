package com.royalenfield.reprime.ui.riderprofile.presenter;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.ui.riderprofile.views.ProfileRidesRowView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.ONGOINGRIDE_TYPE;

/**
 * This is presenter for binding rows in a recyclerview
 */
public class ProfileRidesRowPresenter implements IProfileRidesRowPresenter {

    private String mViewType;

    @Override
    public void onBindProfileRideRowViewAtPosition(int position, ProfileRidesRowView rowView, String mType) {
        mViewType = mType;
        List<ProfileRidesResponse> mProfileRidesResponse = null;
        switch (mType) {
            case REConstants.UPCOMIMGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getUpcomingRideResponse();
                break;
            case REConstants.PASTRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getPastRideResponse();
                break;
            case REConstants.PENDINGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getPendingRideResponse();
                break;
            case REConstants.REJECTEDRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getRejectedRideResponse();
                break;
            case ONGOINGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getOngoingRideResponse();
                break;
        }
        if (mProfileRidesResponse != null) {
            setProfileRiderInformation(mProfileRidesResponse.get(position), rowView);
        }
    }

    private void setProfileRiderInformation(ProfileRidesResponse profileRiderResponse, ProfileRidesRowView rowView) {
        try {
            String websiteImageBaseUrl = REUtils.getMobileappbaseURLs().getAssetsURL();
            if (profileRiderResponse.getRideImages().size() > 0) {
                //Always take first image
                String strRideImage = profileRiderResponse.getRideImages().get(0).getSrcPath();
                rowView.setRideImage(websiteImageBaseUrl + strRideImage);
            } else {
                rowView.setRideImage(websiteImageBaseUrl);
            }
            rowView.setFriendsInvited("");
            String mStartPoint = REUtils.splitPlaceName(profileRiderResponse.getStartPoint());
            String mEndPoint = REUtils.splitPlaceName(profileRiderResponse.getEndPoint());
            rowView.setRideDetail(mStartPoint + " > " + mEndPoint);
            rowView.setRideDate(profileRiderResponse.getRideInfo().getRideStartDateIso(),
                    profileRiderResponse.getRideInfo().getRideEndDateIso());
            if (mViewType.equals(ONGOINGRIDE_TYPE)) {
                rowView.setFriendsInvited(profileRiderResponse.getRideName());
            } else {
                //For all other types of rides
                String createdBy;
                if (profileRiderResponse.getUserInfo() != null && profileRiderResponse.getUserInfo().size() > 0) {
                    createdBy = String.valueOf(profileRiderResponse.getUserInfo().get(REFirestoreConstants.USER_INFO_NAME));
                } else {
                    createdBy = profileRiderResponse.getDealerName();
                }
                rowView.setFriendsInvited(createdBy != null && !createdBy.isEmpty() ?
                        REApplication.getAppContext().getString(R.string.created_by) + " "+createdBy
                        : REApplication.getAppContext().getString(R.string.text_hyphen_na));
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
    public int getRidesRowCount(int count) {
        return count;
    }
}
