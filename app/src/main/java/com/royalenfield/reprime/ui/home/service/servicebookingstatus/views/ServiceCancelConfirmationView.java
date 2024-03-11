package com.royalenfield.reprime.ui.home.service.servicebookingstatus.views;

import com.royalenfield.reprime.base.REMvpView;

public interface ServiceCancelConfirmationView extends REMvpView {

    void onSuccess();

    void onFailure(String errorMessage);
}
