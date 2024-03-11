package com.royalenfield.reprime.models.response.firestore.rides;

import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.models.response.firestore.ourworld.NewsResponse;

import java.util.List;

/**
 * This RERidesModelStore contains the objects for different type of rides
 * Here Different instances are created for different rides.
 */
public class RERidesModelStore {

    private static RERidesModelStore modelStore;
    //Dealer Upcoming Rides
    private List<DealerUpcomingRidesResponse> mDealerUpcomingDealerUpcomingRidesResponse;
    // Popular Rides
    private List<PopularRidesResponse> mPopularRidesResponse;
    // Marquee Rides
    private List<MarqueeRidesResponse> mMarqueeRidesResponse;
    //User upcoming Rides
    private List<UserUpcomingRidesResponse> mUserUpcomingRidesResponse;
    //Our world News
    private List<NewsResponse> mOurWorldNewsResponse;
    //Our world events
    private List<EventsResponse> mOurWorldEventsResponse;


    public static RERidesModelStore getInstance() {
        if (modelStore == null) {
            modelStore = new RERidesModelStore();
        }
        return modelStore;
    }

    /**
     * Clear the model store.
     */
    public static void clearRidesModelStore() {
        modelStore = null;
    }

    public static RERidesModelStore getModelStore() {
        return modelStore;
    }

    public static void setModelStore(RERidesModelStore modelStore) {
        RERidesModelStore.modelStore = modelStore;
    }

    public List<DealerUpcomingRidesResponse> getDealerUpcomingRidesResponse() {
        return mDealerUpcomingDealerUpcomingRidesResponse;
    }

    public void setDealerUpcomingRidesResponse(List<DealerUpcomingRidesResponse> mDealerUpcomingDealerUpcomingRidesResponse) {
        this.mDealerUpcomingDealerUpcomingRidesResponse = mDealerUpcomingDealerUpcomingRidesResponse;
    }

    public List<UserUpcomingRidesResponse> getUserUpcomingRidesResponse() {
        return mUserUpcomingRidesResponse;
    }

    public void setUserUpcomingRidesResponse(List<UserUpcomingRidesResponse> mUserUpcomingRidesResponse) {
        this.mUserUpcomingRidesResponse = mUserUpcomingRidesResponse;
    }

    public List<PopularRidesResponse> getPopularRidesResponse() {
        return mPopularRidesResponse;
    }

    public void setPopularRidesResponse(List<PopularRidesResponse> mMarqueeRidesResponse) {
        this.mPopularRidesResponse = mMarqueeRidesResponse;
    }

    public List<MarqueeRidesResponse> getMarqueeRidesResponse() {
        return mMarqueeRidesResponse;
    }

    public void setMarqueeRidesResponse(List<MarqueeRidesResponse> mMarqueeRidesResponse) {
        this.mMarqueeRidesResponse = mMarqueeRidesResponse;
    }

    public List<NewsResponse> getOurWorldNewsResponse() {
        return mOurWorldNewsResponse;
    }

    public void setOurWorldNewsResponse(List<NewsResponse> mOurWorldNewsResponse) {
        this.mOurWorldNewsResponse = mOurWorldNewsResponse;
    }

    public List<EventsResponse> getOurWorldEventsResponse() {
        return mOurWorldEventsResponse;
    }

    public void setOurWorldEventsResponse(List<EventsResponse> mOurWorldEventsResponse) {
        this.mOurWorldEventsResponse = mOurWorldEventsResponse;
    }
}
