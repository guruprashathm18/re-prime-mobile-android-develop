package com.royalenfield.reprime.ui.home.service.billsummary.views;

import com.royalenfield.reprime.base.REMvpView;

public interface ServiceBillSummaryView extends REMvpView {

    void onSuccess();

    void onFailure(String errorMessage);
}
