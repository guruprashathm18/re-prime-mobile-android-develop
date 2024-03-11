package com.royalenfield.reprime.ui.tripdetail.presenter;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.tripdetail.view.ITripView;
import com.royalenfield.reprime.ui.triplisting.response.TripDeleteResponseModel;

public class TripDetailPresenter implements ITripDetailPresenter, TripDetailCallback {

    private final ITripView mTripView;

    public TripDetailPresenter(ITripView tripView) {
        mTripView = tripView;
    }

    @Override
    public void deleteTrip(String fromDate,String toTs,String tripId) {
        REApplication.getInstance().getREConnectedAPI()
                .sendForTripDeleteEvent(this, fromDate,toTs,tripId);
    }

    @Override
    public void onTripDeleteSuccess(TripDeleteResponseModel tripDeleteResponseModel) {
        if (mTripView!=null) {
            mTripView.finishFragment(tripDeleteResponseModel.getData().getTripDelete());
        }
    }
}
