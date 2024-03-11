package com.royalenfield.reprime.ui.home.service.search.interactor;

import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.home.service.search.view.DealersListRowView;

import java.util.List;

public interface ISearchRowViewpresenter {
    /**
     * For binding the data to a row
     *
     * @param position
     * @param rowView
     * @param mDealersResponse
     * @param fliterFlag
     */
    void onBindDealersListRowViewAtPosition(int position, DealersListRowView rowView,
                                            List<DealerMasterResponse> mDealersResponse, boolean fliterFlag);

    /**
     * For getting the list item count
     *
     * @param mDealersResponseList
     * @return
     */
    int getDealersListRowsCount(List<DealerMasterResponse> mDealersResponseList);


    List<DealerMasterResponse> getFilteredResults(String charSequence, List<DealerMasterResponse> dealersResponseList);


}
