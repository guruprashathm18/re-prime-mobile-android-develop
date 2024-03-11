package com.royalenfield.reprime.ui.triplisting.view;

import java.util.ArrayList;

public interface ITripListingView {
    void hideLoading();

    void updateTripCount(int size);

    void setDataOnView(ArrayList<TripItemModel> tripItemModels);
}
