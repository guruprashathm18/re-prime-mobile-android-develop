package com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor;


import com.royalenfield.reprime.ui.home.service.servicebookingstatus.listener.OnFinishedListener;

public interface IServiceAppointmentDetailsInteractor {

    void getVehicleServiceInProgressList(String mobileNo, OnFinishedListener onFinishedListener);
}
