package com.royalenfield.reprime.ui.home.navigation.interactor;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.googlemap.poi.AutoCompleteSearchResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.POIDetailsResponse;
import com.royalenfield.reprime.models.response.web.navigation.GetLocation;
import com.royalenfield.reprime.ui.home.navigation.listener.NavigationListeners;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

/**
 * @author kiran
 */
public class GoogleMapAPIInteractor implements NavigationListeners.GooglePOIListener {

    private static final String RADIUS = "50000";
    private static final String FIELD_PARAMS = "formatted_address,name,rating,geometry";

    @Override
    public void getPlaces(String query, String aLocation, NavigationListeners.OnPlacesCallFinishedListener listener) {
        REApplication
                .getInstance()
                .getREGoogleMapAPIInstance()
                .getGoogleMapAPI()
                .getPlaceId(REUtils.getREGoogleKeys().getAPIKey(), query, aLocation, aLocation, RADIUS)
                .enqueue(new Callback<AutoCompleteSearchResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<AutoCompleteSearchResponse> call, @NotNull Response<AutoCompleteSearchResponse> response) {
                        RELog.d("API","PlaceId API response code : "+ response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getPredictions() != null && response.body().getPredictions().size() > 0) {
                                RELog.d("API","PlaceId API : "+ response.code());
                                listener.onPlacesSuccess(response.body().getPredictions());
                            } else {
                                RELog.d("API","PlaceId API onResponse");
                                listener.onPlacesFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            RELog.d("API","PlaceId API onResponse : " + response.code() + " : " + response.message());
                            listener.onPlacesFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<AutoCompleteSearchResponse> call, @NotNull Throwable t) {
                        RELog.d("API","PlaceId API failure : "+ t.getMessage());
                        listener.onPlacesFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void getPlaceDetails(String placeId, NavigationListeners.OnPlaceDetailsCallFinishedListener listener) {
        REApplication
                .getInstance()
                .getREGoogleMapAPIInstance()
                .getGoogleMapAPI()
                .getPlaceDetails(REUtils.getREGoogleKeys().getAPIKey(),
                        placeId, FIELD_PARAMS)
                .enqueue(new Callback<POIDetailsResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<POIDetailsResponse> call, @NotNull Response<POIDetailsResponse> response) {
                        RELog.d("API","POI API response code : "+ response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getResult() != null) {
                                RELog.d("API","POI API : "+ response.code());
                                listener.onPlaceDetailsSuccess(response.body().getResult());
                            } else {
                                RELog.d("API","POI API onResponse");
                                listener.onPlaceDetailsFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            RELog.d("API","POI API onResponse : " + response.code() + " : " + response.message());
                            listener.onPlaceDetailsFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<POIDetailsResponse> call, @NotNull Throwable t) {
                        RELog.d("API","POI API failure : "+ t.getMessage());
                        listener.onPlaceDetailsFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void getPlaceName(String aLocation, NavigationListeners.OnPlaceFinishedListener listener) {
        REApplication
                .getInstance()
                .getREGoogleMapAPIInstance()
                .getGoogleMapAPI()
                .getPlaceName(REUtils.getREGoogleKeys().getAPIKey(), aLocation)
                .enqueue(new Callback<GetLocation>() {
                    @Override
                    public void onResponse(@NotNull Call<GetLocation> call, @NotNull Response<GetLocation> response) {
                        RELog.d("API","PlaceId API response code : "+ response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getResults().get(0).getFormattedAddress() != null && !response.body().getResults().get(0).getFormattedAddress().isEmpty()) {
                                RELog.d("API","Place name API : "+ response.code());
                                listener.onPlaceSuccess(response.body().getResults().get(0).getFormattedAddress());
                            } else {
                                RELog.d("API","Place name API onResponse");
                                listener.onPlaceFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            RELog.d("API","Place name API onResponse : " + response.code() + " : " + response.message());
                            listener.onPlaceFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<GetLocation> call, @NotNull Throwable t) {
                        RELog.d("API","Place name API failure : "+ t.getMessage());
                        listener.onPlaceFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}
