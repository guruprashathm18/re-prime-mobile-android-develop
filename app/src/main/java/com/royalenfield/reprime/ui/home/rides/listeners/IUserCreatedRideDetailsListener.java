package com.royalenfield.reprime.ui.home.rides.listeners;

import androidx.cardview.widget.CardView;
import android.view.ViewGroup;

/*
 * This is an interface containing the  abstract methods related to the CardView.
 * */

public interface IUserCreatedRideDetailsListener {

    public final int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();

    Object instantiateitem(ViewGroup container, int position);

}
