package com.royalenfield.reprime.ui.home.service.servicebookingstatus.views;

import com.royalenfield.reprime.base.REMvpView;

public interface ServiceAppointmentDetailsAndStatusView extends REMvpView {

    void onServiceInProgressuccess();

    void onServiceInProgressFailure(String errorMessage);

}
