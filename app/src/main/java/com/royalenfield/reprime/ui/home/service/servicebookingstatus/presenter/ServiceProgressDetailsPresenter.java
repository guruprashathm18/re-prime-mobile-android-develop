package com.royalenfield.reprime.ui.home.service.servicebookingstatus.presenter;

import com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor.ServiceProgressDetailsInteractor;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.views.ServiceAppointmentDetailsAndStatusView;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.listener.OnFinishedListener;

public class ServiceProgressDetailsPresenter implements OnFinishedListener,
        IServiceAppointmentDetailsPresenter {

    private ServiceAppointmentDetailsAndStatusView mServiceAppointmentDetailsAndStatusView;

    private ServiceProgressDetailsInteractor mServiceProgressDetailsInteractor;

    public ServiceProgressDetailsPresenter(ServiceAppointmentDetailsAndStatusView
                                                      serviceAppointmentDetailsAndStatusView,
                                           ServiceProgressDetailsInteractor
                                                      serviceProgressDetailsInteractor) {
        this.mServiceAppointmentDetailsAndStatusView = serviceAppointmentDetailsAndStatusView;
        this.mServiceProgressDetailsInteractor = serviceProgressDetailsInteractor;

    }

    public void onDestroy() {
        mServiceAppointmentDetailsAndStatusView = null;
    }

    @Override
    public void getVehicleServiceInProgressList(String mobileNo) {
        if (mServiceProgressDetailsInteractor != null && mServiceAppointmentDetailsAndStatusView != null) {
            mServiceAppointmentDetailsAndStatusView.showLoading();
            mServiceProgressDetailsInteractor.getVehicleServiceInProgressList(mobileNo, this);
        }
    }

    @Override
    public void onSuccess() {
        if (mServiceAppointmentDetailsAndStatusView != null) {
            mServiceAppointmentDetailsAndStatusView.onServiceInProgressuccess();
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if (mServiceAppointmentDetailsAndStatusView != null) {
            mServiceAppointmentDetailsAndStatusView.onServiceInProgressFailure(errorMessage);
        }
    }
}
