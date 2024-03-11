package com.royalenfield.reprime.ui.home.service.history.interactor;


import com.royalenfield.reprime.models.request.proxy.service.ServiceCenterRequest;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotListResponse;
import com.royalenfield.reprime.ui.home.service.history.listener.ServiceBookingListener;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;

import java.util.List;

public interface IServiceBookingInteractor {

    void doServiceBooking(ServiceBookingRequest serviceBookingRequestList, ServiceBookingListener onServiceBookingListener);

    void getBookingSlots(String branchId, String date, ServiceBookingListener onServiceBookingListener);

    List<ServiceSlotListResponse> groupTimeSlotValues(List<ServiceSlotListResponse> mServiceTimeSlotResponse);

    void getServiceCenters(ServiceCenterRequest serviceCenterRequest, ServiceBookingListener onServiceBookingListener);
}
