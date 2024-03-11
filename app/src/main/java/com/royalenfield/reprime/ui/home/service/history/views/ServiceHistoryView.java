package com.royalenfield.reprime.ui.home.service.history.views;

import com.royalenfield.reprime.base.REMvpView;

public interface ServiceHistoryView extends REMvpView {

    void onServiceHistorySuccess();

    void onServiceHistoryFailure(String errorMessage);
}
