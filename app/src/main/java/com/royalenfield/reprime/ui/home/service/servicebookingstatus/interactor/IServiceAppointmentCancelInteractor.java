package com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor;

import com.royalenfield.reprime.ui.home.service.servicebookingstatus.listener.OnFinishedListener;

public interface IServiceAppointmentCancelInteractor {

    void cancelServiceAppointment(String CaseNo, boolean isDummySlots, OnFinishedListener listener);
}
