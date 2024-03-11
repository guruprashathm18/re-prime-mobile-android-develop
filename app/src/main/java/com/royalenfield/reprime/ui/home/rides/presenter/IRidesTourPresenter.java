package com.royalenfield.reprime.ui.home.rides.presenter;


import com.royalenfield.reprime.ui.home.rides.views.IRidesTourRowView;

public interface IRidesTourPresenter {

    /**
     * For binding the Key Place data to a row
     *
     * @param position
     * @param rowView
     */
    void onRidesTourRowViewAtPosition(int position, IRidesTourRowView rowView, String type, int mListPosition, String viewType);

    /**
     * For getting the key place list item count
     */
    int getRideTourRowsCount(int count);


}
