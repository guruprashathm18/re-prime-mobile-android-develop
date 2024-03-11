package com.royalenfield.reprime.ui.home.service.serviceappointment.presenter;

import com.royalenfield.reprime.ui.home.service.serviceappointment.interactor.ServiceAppointmentInteractor;
import com.royalenfield.reprime.ui.home.service.serviceappointment.listener.OnAppointmentFinishedListener;
import com.royalenfield.reprime.ui.home.service.serviceappointment.view.ServiceAppointmentView;

public class ServiceAppointmentPresenter implements IServiceAppointmentPresenter, OnAppointmentFinishedListener {

    private ServiceAppointmentInteractor mServiceAppointmentInteractor;
    private ServiceAppointmentView mServiceAppointmentView;

    public ServiceAppointmentPresenter(ServiceAppointmentView serviceAppointmentView,
                                       ServiceAppointmentInteractor serviceAppointmentInteractor) {
        this.mServiceAppointmentInteractor = serviceAppointmentInteractor;
        this.mServiceAppointmentView = serviceAppointmentView;
    }


    @Override
    public void onServiceEstimateSuccess(String totalEstimateAmount) {
        if (mServiceAppointmentView != null) {
            mServiceAppointmentView.onServiceEstimateSuccess(totalEstimateAmount);
        }
    }

    @Override
    public void onServiceEstimateFailure(String errorMessage) {
        if (mServiceAppointmentView != null) {
            mServiceAppointmentView.onServiceEstimateFailure(errorMessage);
        }
    }

    @Override
    public void onServiceStatusSuccess() {
        if (mServiceAppointmentView != null) {
            mServiceAppointmentView.onServiceStatusSuccess();
        }
    }

    @Override
    public void onServiceStatusFailure(String errorMessage) {
        if (mServiceAppointmentView != null) {
            mServiceAppointmentView.onServiceStatusFailure(errorMessage);
        }
    }

    @Override
    public void onGeneratePaymentSuccess(String message) {
        if (mServiceAppointmentView != null) {
            mServiceAppointmentView.onGeneratePaymentSuccess(message);
        }
    }

    @Override
    public void onGeneratePaymentFailure(String errorMessage) {
        if (mServiceAppointmentView != null) {
            mServiceAppointmentView.onGeneratePaymentFailure(errorMessage);
        }
    }

    @Override
    public void getServiceEstimate(String RegistrationNo) {
        if (mServiceAppointmentView != null && mServiceAppointmentInteractor != null) {
            mServiceAppointmentView.showLoading();
            mServiceAppointmentInteractor.getServiceEstimate(RegistrationNo, this);
        }
    }

    @Override
    public void getServiceStatus(String CaseNo) {
        if (mServiceAppointmentView != null && mServiceAppointmentInteractor != null) {
            mServiceAppointmentView.showLoading();
            mServiceAppointmentInteractor.getServiceStatus(CaseNo, this);
        }
    }

    @Override
    public void generatePaymentMessage(String txnAmount, String caseId, String currencyType) {
        if (mServiceAppointmentView != null && mServiceAppointmentInteractor != null) {
            mServiceAppointmentView.showLoading();
            mServiceAppointmentInteractor.generatePaymentMessage(txnAmount, caseId, currencyType, this);
        }
    }
}
