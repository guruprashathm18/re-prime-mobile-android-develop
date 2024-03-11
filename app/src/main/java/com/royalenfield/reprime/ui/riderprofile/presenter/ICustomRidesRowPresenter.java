package com.royalenfield.reprime.ui.riderprofile.presenter;

import com.royalenfield.reprime.ui.riderprofile.views.CustomRidesRowView;

public interface ICustomRidesRowPresenter {

    /**
     * For binding the data to row
     * @param position : position of row
     * @param rowView : CustomRidesRowView
     */
    void onBindPreviousRideRowViewAtPosition(int position, CustomRidesRowView rowView, String type);

    /**
     * For getting the list items count
     * @return no of items
     */
    int getRidesRowCount(int count);
}
