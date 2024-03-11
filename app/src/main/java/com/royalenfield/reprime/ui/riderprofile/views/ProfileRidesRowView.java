package com.royalenfield.reprime.ui.riderprofile.views;

/**
 * Interface for binding upcoming rides RecyclerView rows
 */
public interface ProfileRidesRowView {

    void setFriendsInvited(String friendsInvited);

    void setRideDetail(String rideDetail);

    void setRideDate(String rideStartDate, String rideEndDate);

    void setRideImage(String imagePath);
}
