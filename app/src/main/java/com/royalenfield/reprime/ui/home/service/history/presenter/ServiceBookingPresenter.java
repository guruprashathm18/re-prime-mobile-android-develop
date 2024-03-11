package com.royalenfield.reprime.ui.home.service.history.presenter;

import com.royalenfield.reprime.models.request.proxy.service.SearchServiceCentreRequest;
import com.royalenfield.reprime.models.request.proxy.service.ServiceCenterRequest;
import com.royalenfield.reprime.ui.home.service.history.listener.ServiceBookingListener;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;
import com.royalenfield.reprime.ui.home.service.history.interactor.ServiceBookingInteractor;


public class ServiceBookingPresenter implements IServiceBookingPresenter {

    private ServiceBookingListener mServiceBookingView;
    private ServiceBookingInteractor mServiceBookingInteractor;


    public ServiceBookingPresenter(ServiceBookingListener serviceBookingListener, ServiceBookingInteractor serviceBookingInteractor) {
        this.mServiceBookingView = serviceBookingListener;
        this.mServiceBookingInteractor = serviceBookingInteractor;
    }

    public void onDestroy() {
        mServiceBookingView = null;
    }

    @Override
    public void doServiceBooking(ServiceBookingRequest serviceBookingRequest) {
        mServiceBookingInteractor.doServiceBooking(serviceBookingRequest, mServiceBookingView);
    }

    @Override
    public void getBookingSlots(String appointmentDate, String branchID) {
        mServiceBookingInteractor.getBookingSlots(appointmentDate, branchID, mServiceBookingView);
    }

    @Override
    public void getServiceCenters(ServiceCenterRequest serviceCenterRequest) {
        mServiceBookingInteractor.getServiceCenters(serviceCenterRequest, mServiceBookingView);
    }

    public void getServiceCentreSearchResults(SearchServiceCentreRequest searchServiceCentreRequest){
        mServiceBookingInteractor.getServiceCentreSearchResults(searchServiceCentreRequest,mServiceBookingView);
    }

    public void getPickupAndDoorstepServiceSlots(String dealerId){
        mServiceBookingInteractor.getPickupAndDoorstepServiceResults(dealerId,mServiceBookingView);
    }
}
