package com.royalenfield.reprime.ui.home.service.history.presenter;

import com.royalenfield.reprime.models.request.proxy.service.ServiceCenterRequest;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;


public interface IServiceBookingPresenter {

    void doServiceBooking(ServiceBookingRequest serviceBookingRequestList);

    void getBookingSlots(String branchID, String date);

    void getServiceCenters(ServiceCenterRequest serviceCenterRequest);
}
