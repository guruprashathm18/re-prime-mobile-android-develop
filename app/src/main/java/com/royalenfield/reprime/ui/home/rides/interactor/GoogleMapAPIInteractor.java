package com.royalenfield.reprime.ui.home.rides.interactor;

import android.util.Log;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.googlemap.directions.DirectionsResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.NearBySearchResponse;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapAPIInteractor implements RidesListeners.GooglePOIListener {
    @Override
    public void getPOI(String location, RidesListeners.OnPOICallFinishedListener listener) {
        REApplication
                .getInstance()
                .getREGoogleMapAPIInstance()
                .getGoogleMapAPI()
                .getPOI(REUtils.getREGoogleKeys().getRideServiceAPIKey(),
                        location, "distance")
                .enqueue(new Callback<NearBySearchResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<NearBySearchResponse> call, @NotNull Response<NearBySearchResponse> response) {
                        Log.d("API", "POI API response code : " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getResults() != null && response.body().getResults().size() > 0) {
                                Log.d("API", "POI API : " + response.code());
                                listener.onPOISuccess(response.body());
                            } else {
                                Log.d("API", "POI API onResponse");
                                listener.onPOIFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            Log.d("API", "POI API onResponse : " + response.code() + " : " + response.message());
                            listener.onPOIFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<NearBySearchResponse> call, @NotNull Throwable t) {
                        Log.d("API", "POI API failure : " + t.getMessage());
                        listener.onPOIFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void getPlaces(String query, RidesListeners.OnPlacesCallFinishedListener listener) {
        REApplication
                .getInstance()
                .getREGoogleMapAPIInstance()
                .getGoogleMapAPI()
                .getPlaces(REUtils.getREGoogleKeys().getRideServiceAPIKey(),
                        query, "in")
                .enqueue(new Callback<NearBySearchResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<NearBySearchResponse> call, @NotNull Response<NearBySearchResponse> response) {
                        Log.d("API", "POI API response code : " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getResults() != null && response.body().getResults().size() > 0) {
                                Log.d("API", "POI API : " + response.code());
                                listener.onPlaceSuccess(response.body().getResults());
                            } else {
                                Log.d("API", "POI API onResponse");
                                listener.onPlaceFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            Log.d("API", "POI API onResponse : " + response.code() + " : " + response.message());
                            listener.onPlaceFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<NearBySearchResponse> call, @NotNull Throwable t) {
                        Log.d("API", "POI API failure : " + t.getMessage());
                        listener.onPlaceFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void getDirections(String origin, String destination, String waypoints, RidesListeners.OnDirectionsCallFinishedListener listener) {
        REApplication
                .getInstance()
                .getREGoogleMapAPIInstance()
                .getGoogleMapAPI()
                .getDirections(REUtils.getREGoogleKeys().getRideServiceAPIKey(),
                        origin, destination, "in",
                        "driving", waypoints,
                        "metric")
                .enqueue(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<DirectionsResponse> call, @NotNull Response<DirectionsResponse> response) {
                        Log.d("API", "POI API response code : " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getRoutes() != null && response.body().getRoutes().size() > 0) {
                                Log.d("API", "POI API : " + response.code());
                                listener.onDirectionsSuccess(response.body().getRoutes());
                            } else {
                                Log.d("API", "POI API onResponse");
                                listener.onDirectionsFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            Log.d("API", "POI API onResponse : " + response.code() + " : " + response.message());
                            listener.onDirectionsFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<DirectionsResponse> call, @NotNull Throwable t) {
                        Log.d("API", "POI API failure : " + t.getMessage());
                        listener.onDirectionsFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}
