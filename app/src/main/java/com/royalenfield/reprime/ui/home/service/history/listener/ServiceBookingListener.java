package com.royalenfield.reprime.ui.home.service.history.listener;

import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.PickupandDoorstepServiceSlot;

import java.util.List;

public interface ServiceBookingListener {

    void onServiceBookingSuccess();

    void onServiceBookingFailure(String errorMessage);

    void onServiceTimeSlotSuccess();

    void onServiceTimeSlotFailure(String errorMessage);

    void onServiceCenterFetchSuccess(List<DealerMasterResponse> response);

    void onServiceCenterFetchFailed(String errorMessage);

    void ongetPickupAndDoorstepServiceSlotsSuccess();

    void ongetPickupAndDoorstepServiceSlotsFailure();
}
