package com.royalenfield.reprime.ui.home.service.serviceappointment.interactor;

import com.royalenfield.reprime.ui.home.service.serviceappointment.listener.OnAppointmentFinishedListener;

public interface IServiceAppointmentInteractor {

    void getServiceEstimate(String getServiceEstimate, OnAppointmentFinishedListener listener);

    void getServiceStatus(String caseNo, OnAppointmentFinishedListener listener);

    void generatePaymentMessage(String txnAmount, String caseId, String currencyType, OnAppointmentFinishedListener listener);
}
