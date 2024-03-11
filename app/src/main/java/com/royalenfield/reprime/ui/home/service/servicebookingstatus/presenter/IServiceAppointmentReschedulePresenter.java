package com.royalenfield.reprime.ui.home.service.servicebookingstatus.presenter;

public interface IServiceAppointmentReschedulePresenter {

    void rescheduleServiceAppointment(String AppointmentId, String AppointmentDate,
                                      String AttachmentKey, String CustomerRemarks,
                                      String DropAddress,
                                      String SlotID, String IsPickUpDrop, String PickUpAddress,
                                      String ServiceType,
                                      String Creation_Source);
}
