package com.royalenfield.reprime.ui.triplisting.presenter;

public interface ITripListingPresenter {
    void createConnectionForTripData();

    void getStubbedData();

    void getTripDataWithFilter(String fromDate, String toDate);

}
