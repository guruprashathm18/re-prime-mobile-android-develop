package com.royalenfield.reprime.ui.home.service.serviceappointment.presenter;

public interface IServiceAppointmentPresenter {

    void getServiceEstimate(String RegistrationNo);

    void getServiceStatus(String CaseNo);

    void generatePaymentMessage(String txnAmount, String caseId, String currencyType);
}
