package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.rides.views.IRidesTourRowView;
import com.royalenfield.reprime.utils.REConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.OUR_WORLD_EVENTS;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_MARQUEE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_POPULAR;
import static com.royalenfield.reprime.utils.REFirestoreConstants.EVENT_HIGHLIGHTS;
import static com.royalenfield.reprime.utils.REFirestoreConstants.POPULAR_RIDES_ITINERY_DATE;
import static com.royalenfield.reprime.utils.REFirestoreConstants.POPULAR_RIDES_ITINERY_SUMMARY;
import static com.royalenfield.reprime.utils.REFirestoreConstants.POPULAR_RIDES_KEY_PLACE_NAME;
import static com.royalenfield.reprime.utils.REFirestoreConstants.RIDE_IMAGE_PATH;

/**
 * Common presenter for Key places, Itenerary and Galley
 */
public class RidesTourPresenter implements IRidesTourPresenter {

    private String mRideType;
    private IRidesTourRowView mRowView;
    private int mPosition;
    private ArrayList<Map<String, Object>> mItenerary = new ArrayList<>();
    private ArrayList<Map<String, Object>> mWaypoints = new ArrayList<>();
    private ArrayList<Map<String, String>> mGallery = new ArrayList<>();


    @Override
    public void onRidesTourRowViewAtPosition(int position, IRidesTourRowView rowView, String ridetype,
                                             int mListItemPosition, String viewtype) {
        try {
            mRideType = ridetype;
            mRowView = rowView;
            mPosition = position;
            List<PopularRidesResponse> mPopularRidesResponse = RERidesModelStore.getInstance().getPopularRidesResponse();
            List<MarqueeRidesResponse> mMarqueeRidesResponse = RERidesModelStore.getInstance().getMarqueeRidesResponse();
            List<EventsResponse> mEventsResponse = RERidesModelStore.getInstance().getOurWorldEventsResponse();
            switch (viewtype) {
                case REConstants.KEY_PLACES:
                    if (ridetype.equals(RIDE_TYPE_POPULAR)) {
                        mWaypoints = mPopularRidesResponse.get(mListItemPosition).getWaypoints();
                    } else if (ridetype.equals(RIDE_TYPE_MARQUEE)) {
                        mWaypoints = mMarqueeRidesResponse.get(mListItemPosition).getWaypoints();
                    }
                    setWaypointsData();
                    break;
                case REConstants.ITINERARY:
                    switch (ridetype) {
                        case OUR_WORLD_EVENTS:
                            mItenerary = mEventsResponse.get(mListItemPosition).getItinerary();
                            break;
                        case RIDE_TYPE_POPULAR:
                            mItenerary = mPopularRidesResponse.get(mListItemPosition).getItinerary();
                            break;
                        case RIDE_TYPE_MARQUEE:
                            mItenerary = mMarqueeRidesResponse.get(mListItemPosition).getItinerary();
                            break;
                    }
                    setIteneraryData();
                    break;
                case REConstants.KEY_RIDE_GALLERY:
                    switch (ridetype) {
                        case OUR_WORLD_EVENTS:
                            mGallery = mEventsResponse.get(mListItemPosition).getGallery();
                            break;
                        case RIDE_TYPE_POPULAR:
                            mGallery = mPopularRidesResponse.get(mListItemPosition).getGallery();
                            break;
                        case RIDE_TYPE_MARQUEE:
                            mGallery = mMarqueeRidesResponse.get(mListItemPosition).getGallery();
                            break;
                    }
                    setGalleryData();
                    break;
                default:
                    break;

            }
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * Sets the waypoint data to a row
     */
    private void setWaypointsData() {
        if (mWaypoints != null && mWaypoints.size() > 0) {
            String mWaypointsName = String.valueOf(mWaypoints.get(mPosition).get(POPULAR_RIDES_KEY_PLACE_NAME));
            mRowView.setKeyPlaceName(!mWaypointsName.equals("null") && !mWaypointsName.isEmpty() ? mWaypointsName :
                    REApplication.getAppContext().getResources().getString(R.string.text_hyphen_na));
        }
    }

    /**
     * Sets the itenerary data to a row
     */
    private void setIteneraryData() {
        String mDate, mDescription;
        if (mRideType.equals(RIDE_TYPE_POPULAR) || mRideType.equals(RIDE_TYPE_MARQUEE)) {
            if (mItenerary != null && mItenerary.size() > 0) {
                mDate = String.valueOf(mItenerary.get(mPosition).get(POPULAR_RIDES_ITINERY_DATE));
                mDescription = String.valueOf(mItenerary.get(mPosition).get(POPULAR_RIDES_ITINERY_SUMMARY));
                mRowView.setItineraryDate(!mDate.equals("null") && !mDate.isEmpty() ? mDate :
                        REApplication.getAppContext().getResources().getString(R.string.text_hyphen_na));
                mRowView.setItineraryDesciption(!mDescription.isEmpty() ? mDescription :
                        REApplication.getAppContext().getResources().getString(R.string.text_hyphen_na));
            }
        } else {
            if (mItenerary != null && mItenerary.size() > 0) {
                mDate = String.valueOf(mItenerary.get(mPosition).get(POPULAR_RIDES_ITINERY_DATE));
                mRowView.setItineraryDate(!mDate.equals("null") && !mDate.isEmpty() ? mDate :
                        REApplication.getAppContext().getResources().getString(R.string.text_hyphen_na));
                ArrayList<Map<String, Object>> mEventHighlights =
                        (ArrayList<Map<String, Object>>) mItenerary.get(mPosition).get(EVENT_HIGHLIGHTS);
                mRowView.setEventHighlightsRecyclerView(mEventHighlights != null && mEventHighlights.size() > 0 ?
                        mEventHighlights : null);
            }
        }
    }

    /**
     * Sets the gallery data to a row
     */
    private void setGalleryData() {
        if (mGallery != null && mGallery.size() > 0) {
            String mImagePath = String.valueOf(mGallery.get(mPosition).get(RIDE_IMAGE_PATH));
            mRowView.setRideGalleryImages(!mImagePath.equals("null") && !mImagePath.isEmpty() ? mImagePath : "");
        }
    }

    @Override
    public int getRideTourRowsCount(int count) {
        return count;
    }

}
