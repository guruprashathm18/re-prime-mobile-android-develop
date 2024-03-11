package com.royalenfield.reprime.ui.home.service.history.presenter;

import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;
import com.royalenfield.reprime.ui.home.service.history.views.ServiceHistoryRowView;

import java.util.List;

public interface IServiceHistoryRowPresenter {

    /**
     * For binding the data to a row
     *
     * @param position
     * @param rowView
     * @param mServiceHistoryResponse
     */
    void onBindServiceHistoryRowViewAtPosition(int position, ServiceHistoryRowView rowView,
                                               List<ServiceHistoryResponse> mServiceHistoryResponse);

    /**
     * For getting the list item count
     *
     * @param mServiceHistoryResponse
     * @return
     */
    int getRepositoriesRowsCount(List<ServiceHistoryResponse> mServiceHistoryResponse);
}
