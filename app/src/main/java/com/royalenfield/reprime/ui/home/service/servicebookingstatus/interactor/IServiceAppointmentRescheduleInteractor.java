package com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor;

import com.royalenfield.reprime.ui.home.service.listener.OnServiceRescheduleResponseListener;

public interface IServiceAppointmentRescheduleInteractor {

    void rescheduleServiceAppointment(String AppointmentId, String AppointmentDate,
                                      String AttachmentKey, String CustomerRemarks,
                                      String DropAddress,
                                      String SlotID, String IsPickUpDrop, String PickUpAddress,
                                      String ServiceType,
                                      String Creation_Source,String BranchID,String regNo, boolean isDummySlots, OnServiceRescheduleResponseListener onServiceRescheduleResponseListener);
}
