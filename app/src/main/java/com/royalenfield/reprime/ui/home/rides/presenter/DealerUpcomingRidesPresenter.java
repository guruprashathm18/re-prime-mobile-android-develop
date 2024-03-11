package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.ui.home.rides.views.DealerUpcomingRidesView;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REFirestoreConstants.RIDE_IMAGE_PATH;

public class DealerUpcomingRidesPresenter implements IDealerUpcomingRidesPresenter {

    private List<DealerUpcomingRidesResponse> mDealerUpcomingRidesResponse;

    @Override
    public void onBindPreviousRideRowViewAtPosition(int position, DealerUpcomingRidesView rowView,
                                                    List<DealerUpcomingRidesResponse> dealerUpcomingRidesResponse) {
        try {
            this.mDealerUpcomingRidesResponse = new ArrayList<>();
            this.mDealerUpcomingRidesResponse = dealerUpcomingRidesResponse;
            rowView.setDealerName(dealerUpcomingRidesResponse.get(position).getDealerName());
            rowView.setRideName(dealerUpcomingRidesResponse.get(position).getRideName());
            rowView.setRideImage(dealerUpcomingRidesResponse.get(position).getRideImages().get(0).get(RIDE_IMAGE_PATH));
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    public int getRidesRowCount() {
        if (mDealerUpcomingRidesResponse != null && mDealerUpcomingRidesResponse.size() > 0) {
            return mDealerUpcomingRidesResponse.size();
        } else {
            return 0;
        }
    }
}
