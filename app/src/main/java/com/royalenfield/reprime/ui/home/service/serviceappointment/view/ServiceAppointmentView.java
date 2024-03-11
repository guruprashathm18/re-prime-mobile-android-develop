package com.royalenfield.reprime.ui.home.service.serviceappointment.view;

import com.royalenfield.reprime.base.REMvpView;

public interface ServiceAppointmentView extends REMvpView {

    void onServiceEstimateSuccess(String totalEstimateAmount);

    void onServiceEstimateFailure(String errorMessage);

    void onServiceStatusSuccess();

    void onServiceStatusFailure(String errorMessage);

    void onGeneratePaymentSuccess(String message);

    void onGeneratePaymentFailure(String errorMessage);

}
