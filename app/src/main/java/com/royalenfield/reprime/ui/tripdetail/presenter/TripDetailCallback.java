package com.royalenfield.reprime.ui.tripdetail.presenter;

import com.royalenfield.reprime.ui.triplisting.response.TripDeleteResponseModel;

public interface TripDetailCallback {
    void onTripDeleteSuccess(TripDeleteResponseModel tripDeleteResponseModel);
}
