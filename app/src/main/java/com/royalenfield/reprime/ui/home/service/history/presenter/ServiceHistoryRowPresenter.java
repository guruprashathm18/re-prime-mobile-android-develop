package com.royalenfield.reprime.ui.home.service.history.presenter;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;
import com.royalenfield.reprime.ui.home.service.history.views.ServiceHistoryRowView;

import java.util.List;

public class ServiceHistoryRowPresenter implements IServiceHistoryRowPresenter {

    @Override
    public void onBindServiceHistoryRowViewAtPosition(int position, ServiceHistoryRowView rowView, List<ServiceHistoryResponse> mServiceHistoryResponse) {
        if (mServiceHistoryResponse != null && mServiceHistoryResponse.size() > 0) {
            if (rowView != null) {
                //TODO need to check with api data
                String mInvoiceAmount = mServiceHistoryResponse.get(position).getBillAmount();
                String mInvoiceDate = mServiceHistoryResponse.get(position).getInvoiceDate();
                rowView.SetInvoiceAmount(mInvoiceAmount != null && !mInvoiceAmount.isEmpty() ? mInvoiceAmount :
                        REApplication.getAppContext().getResources().getString(R.string.text_hyphen_na));
                rowView.setInvoiceDate(mInvoiceDate != null && !mInvoiceDate.isEmpty() ? mInvoiceDate :
                        REApplication.getAppContext().getResources().getString(R.string.text_hyphen_na));
            }
        }
    }

    @Override
    public int getRepositoriesRowsCount(List<ServiceHistoryResponse> mServiceHistoryResponse) {
        if (mServiceHistoryResponse != null) {
            return mServiceHistoryResponse.size();
        } else {
            return 0;
        }
    }
}
