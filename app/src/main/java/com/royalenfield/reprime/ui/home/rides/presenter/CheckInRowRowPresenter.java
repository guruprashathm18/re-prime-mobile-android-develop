package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.ui.home.rides.views.CheckInRowView;
import com.royalenfield.reprime.utils.REUtils;

import com.royalenfield.reprime.utils.RELog;

public class CheckInRowRowPresenter implements ICheckInRowPresenter {

    private CheckInResponse mCheckInResponse;

    @Override
    public void onBindCheckInViewAtPosition(int listPosition, CheckInRowView checkInRowView) {
        try {
            checkInRowView.setCheckInLocation(mCheckInResponse.getData().get(listPosition).getAddress());
            checkInRowView.setCheckInPlaceName(mCheckInResponse.getData().get(listPosition).getCheckInPlaceName());
            checkInRowView.setCheckInType(mCheckInResponse.getData().get(listPosition).getCheckInCategory());
            checkInRowView.setCheckInPlaceImage(REUtils.getMobileappbaseURLs().getAssetsURL() + mCheckInResponse.getData().get(listPosition).getCheckInThumbnailImage());
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    @Override
    public int getCheckInCount(CheckInResponse checkInResponse) {
        mCheckInResponse = checkInResponse;
        if (checkInResponse != null && checkInResponse.getData() != null && checkInResponse.getData().size() > 0) {
            return checkInResponse.getData().size();
        } else {
            return 0;
        }
    }
}
