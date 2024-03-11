package com.royalenfield.reprime.ui.home.service.servicebookingstatus.presenter;

import android.util.Log;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.listener.OnFinishedListener;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.views.ServiceCancelConfirmationView;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor.ServiceAppointmentCancelInteractor;

public class ServiceAppointmentCancelPresenter implements IServiceAppointmentCancelPresenter, OnFinishedListener {

    private ServiceCancelConfirmationView mServiceCancelConfirmationView;

    private ServiceAppointmentCancelInteractor mServiceAppointmentCancelInteractor;

    public ServiceAppointmentCancelPresenter(ServiceCancelConfirmationView serviceCancelConfirmationView,
                                             ServiceAppointmentCancelInteractor serviceAppointmentCancelInteractor) {
        this.mServiceAppointmentCancelInteractor = serviceAppointmentCancelInteractor;
        this.mServiceCancelConfirmationView = serviceCancelConfirmationView;
    }

    @Override
    public void cancelServiceAppointment(String CaseNo, boolean isDummySlots) {
        if (mServiceAppointmentCancelInteractor != null && mServiceCancelConfirmationView != null) {
            mServiceCancelConfirmationView.showLoading();
            Log.d("API", "DMS cancelServiceAppointment called");
            mServiceAppointmentCancelInteractor.cancelServiceAppointment(CaseNo, isDummySlots, this);
        }
    }

    @Override
    public void onSuccess() {
        if (mServiceCancelConfirmationView != null) {
            mServiceCancelConfirmationView.onSuccess();
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if (mServiceCancelConfirmationView != null) {
            mServiceCancelConfirmationView.onFailure(errorMessage);
        }
    }
}
