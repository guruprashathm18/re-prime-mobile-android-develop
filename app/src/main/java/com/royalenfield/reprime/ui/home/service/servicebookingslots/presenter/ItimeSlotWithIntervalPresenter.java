package com.royalenfield.reprime.ui.home.service.servicebookingslots.presenter;

import java.util.List;

public interface ItimeSlotWithIntervalPresenter {

    List<String> getSlots(String slotName, List<String> timeSlots);

    List<String> filterSlots(String slotName, List<String> timeSlots);
}
