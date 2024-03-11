package com.royalenfield.reprime.ui.home.service.serviceappointment.listener;

public interface OnAppointmentFinishedListener {

    void onServiceEstimateSuccess(String totalEstimateAmount);

    void onServiceEstimateFailure(String errorMessage);

    void onServiceStatusSuccess();

    void onServiceStatusFailure(String errorMessage);

    void onGeneratePaymentSuccess(String message);

    void onGeneratePaymentFailure(String errorMessage);

}
