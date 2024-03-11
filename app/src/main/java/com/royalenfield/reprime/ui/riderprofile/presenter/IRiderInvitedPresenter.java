package com.royalenfield.reprime.ui.riderprofile.presenter;

import com.royalenfield.reprime.ui.home.rides.views.IRidesTourRowView;

public interface IRiderInvitedPresenter {
        /**
         * For binding the Key Place data to a row
         *
         * @param position
         * @param rowView
         */
        void onRiderInvitedRowViewAtPosition(int position, IRidesTourRowView rowView, int mListPosition, String type);

        /**
         * For getting the key place list item count
         */
        int getRideTourRowsCount(int count);


}
