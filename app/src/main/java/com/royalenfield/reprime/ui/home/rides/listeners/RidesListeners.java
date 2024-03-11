package com.royalenfield.reprime.ui.home.rides.listeners;

import android.view.ViewGroup;

import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.models.response.googlemap.directions.Route;
import com.royalenfield.reprime.models.response.googlemap.poi.NearBySearchResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;

import java.util.List;

public class RidesListeners {

    public interface ICheckInPresenter {

        void getCheckIns(String rideId);

        void publishRide(String rideId);

        void startRide(String rideId);

        void endRide(String rideId);

    }

    public interface ICheckInInteractor {

        void getCheckIns(String rideId, OnCheckInAPIFinishedListener listener);

        void publishRide(String rideId, OnPublishRideAPIFinishedListener listener);

        void startRide(String rideId, OnUpdateRideStatusAPIFinishedListener listener);

        void endRide(String rideId, OnUpdateRideStatusAPIFinishedListener listener);

    }

    public interface OnCheckInAPIFinishedListener {

        void onSuccess(CheckInResponse response);

        void onFailure(String errorMessage);

    }

    public interface OnUpdateRideStatusAPIFinishedListener {

        void onUpdateRideStatusSuccess();

        void onUpdateRideStatusFailure(String errorMessage);

    }

    public interface OnPublishRideAPIFinishedListener {

        void onPublishRideSuccess();

        void onPublishRideFailure(String errorMessage);

    }

    /**
     * This interface is to update the location in Create & PlanRide screens
     */
    public interface CurrentLocationListener {

        void updateCurrentLocation(LatLng latLng);

        void setDistance(Double iDistanceKM);
    }

    /**
     * This interface is to update the location in Create & PlanRide screens
     */
    public interface RouteProgressListener {

        void setRemainingDistance(Double iDistanceKM);

        void setRemainigDuration(long duration);
    }


    public interface IOnCreateRideFinishedListener {

        void onSuccess();

        void onFailure(String errorMessage);
    }


    public interface NavigationImageUploadListener {

        void onSuccess(List<String> imageUrlList);

        void onFailure(String errorMessage);
    }

    /*
     * This is an interface containing the  abstract methods related to the CardView.
     * */

    public interface IUserCreatedRideDetailsListener {

        int MAX_ELEVATION_FACTOR = 8;

        float getBaseElevation();

        CardView getCardViewAt(int position);

        int getCount();

        Object instantiateitem(ViewGroup container, int position);

    }

    public interface OnAPICallFinishedListener {

        void onSuccess();

        void onFailure(String errorMessage);
    }

    public interface RideGetSelectedTimeListener {

        void getSelectedTime(String time);

        void getSelectedEndTime(String time);

        boolean getSelectedDate();

        boolean getSelectedEndDate();
    }

    /**
     * Listener to enable/disable the time adapter
     */
    public interface RideTimeListener {

        void rideTimeEnable();

        void rideEndTimeEnable();

        void rideTimeDisable();

        void rideEndTimeDisable();

        void changeSelectedTimeImage(String time);

        void changeSelectedEndTimeImage(String time);

        void selectTimeForModifyRide(String time);

        void selectEndTimeForModifyRide(String time);


    }
    public interface GooglePOIListener {
        void getPOI(String location, OnPOICallFinishedListener listener);

        void getPlaces(String query, OnPlacesCallFinishedListener listener);

        void getDirections(String origin, String destination, String waypoints, OnDirectionsCallFinishedListener listener);
    }

    public interface OnPOICallFinishedListener {

        void onPOISuccess(NearBySearchResponse nearBySearchResponse);

        void onPOIFailure(String error);
    }

    public interface OnPlacesCallFinishedListener {

        void onPlaceSuccess(List<POIResults> nearBySearchResponse);

        void onPlaceFailure(String error);
    }

    public interface OnDirectionsCallFinishedListener {

        void onDirectionsSuccess(List<Route> nearBySearchResponse);

        void onDirectionsFailure(String error);
    }
}
