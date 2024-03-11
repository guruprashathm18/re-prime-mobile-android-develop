package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.ui.home.rides.views.DealerUpcomingRidesView;

import java.util.List;

public interface IDealerUpcomingRidesPresenter {

    /**
     * For binding the data to row
     *
     * @param position : position of row
     * @param rowView  : DealerUpcomingRidesView
     */
    void onBindPreviousRideRowViewAtPosition(int position, DealerUpcomingRidesView rowView,
                                             List<DealerUpcomingRidesResponse> dealerUpcomingRidesResponse);

    /**
     * For getting the list items count
     *
     * @return no of items
     */
    int getRidesRowCount();
}
