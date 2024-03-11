package com.royalenfield.reprime.ui.riderprofile.views;

import android.text.SpannableStringBuilder;

/**
 * Interface for binding  previous rides RecyclerView rows
 */
public interface CustomRidesRowView {

    void setRideDetail(String rideDetail);

    void setRideDate(SpannableStringBuilder rideDate);

    void setRideImage(String url);

    void setRideLogoImage(String url);

    void setRideOwner(String owner);
}
