package com.royalenfield.reprime.ui.riderprofile.presenter;

import com.royalenfield.reprime.ui.riderprofile.views.ProfileRidesRowView;

public interface IProfileRidesRowPresenter {

    /**
     * For binding the data to row
     * @param position : position of row
     * @param rowView : ProfileRidesRowView
     */
    void onBindProfileRideRowViewAtPosition(int position, ProfileRidesRowView rowView, String type);

    /**
     * For getting the list items count
     * @return count
     */
    int getRidesRowCount(int count);

}
