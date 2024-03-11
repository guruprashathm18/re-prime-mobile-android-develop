package com.royalenfield.reprime.ui.home.service.servicebookingslots.presenter;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotWithIntervalPresenter implements ItimeSlotWithIntervalPresenter {

    /**
     * returns slot list based on slot category
     *
     * @param slotName  :: slotCategory such as morning ,afterNoon and evening
     * @param timeSlots ::List of timeSlots values in string format such as 09:00,09:30
     * @return returns slot list based on slot category.
     */
    @Override
    public List<String> getSlots(String slotName, List<String> timeSlots) {
        List<String> slotsList = null;
        if (timeSlots != null && timeSlots.size() > 0) {
            slotsList = filterSlots(slotName, timeSlots);
        }
        return slotsList;
    }

    /**
     * returns slot list based on slot category
     *
     * @param slotName  :: slotCategory such as morning ,afterNoon and evening
     * @param timeSlots ::List of timeSlots values in string format such as 09:00,09:30
     * @return returns slot list based on slot category.
     */

    @Override
    public List<String> filterSlots(String slotName, List<String> timeSlots) {
        List<String> slotsList = null;
        if (slotName.equalsIgnoreCase(REApplication.getAppContext().getResources().getString(R.string.Morning))) {
            slotsList = new ArrayList<String>();
            for (String slots : timeSlots) {
                if (slots != null) {
                    if (Integer.parseInt(slots.substring(0, 2)) >= REApplication.getAppContext().getResources().
                            getInteger(R.integer.morning_slot_interval_start) && Integer.parseInt(slots.substring(0, 2)) < REApplication.getAppContext().getResources().getInteger(R.integer.morning_slot_interval_ends)) {
                        slotsList.add(slots);
                    }
                }
            }
        } else if (slotName.equalsIgnoreCase(REApplication.getAppContext().getResources().getString(R.string.Afternoon))) {
            slotsList = new ArrayList<String>();
            for (String slots : timeSlots) {
                if (slots != null) {
                    if (Integer.parseInt(slots.substring(0, 2)) >= REApplication.getAppContext().getResources().getInteger(R.integer.afternoon_slot_interval_start) && Integer.parseInt(slots.substring(0, 2)) < REApplication.getAppContext().getResources().getInteger(R.integer.afternoon_slot_interval_ends)) {
                        slotsList.add(slots);
                    }
                }
            }

        } else if (slotName.equalsIgnoreCase(REApplication.getAppContext().getResources().getString(R.string.Evening))) {
            slotsList = new ArrayList<String>();
            for (String slots : timeSlots) {
                if (slots != null) {
                    if (Integer.parseInt(slots.substring(0, 2)) >= REApplication.getAppContext().getResources().getInteger(R.integer.evening_slot_interval_start)) {
                        slotsList.add(slots);
                    }
                }
            }
        }
        return slotsList;
    }
}

