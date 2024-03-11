package com.royalenfield.reprime.ui.home.rides.interactor;

import android.util.Log;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.models.response.web.publishride.RideResponse;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author BOP1KOR on 4/1/2019.
 * <p>
 * Interactor class for Rides related API communication with the Web-Sererer
 */
public class RidesInteractor implements RidesListeners.ICheckInInteractor {

    private String TAG = RidesInteractor.class.getSimpleName();

    private String jwtToken = getJwtToken();

    @Override
    public void getCheckIns(String rideId, RidesListeners.OnCheckInAPIFinishedListener listener) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .getCheckIns(rideId)
                .enqueue(new BaseCallback<CheckInResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<CheckInResponse> call, @NotNull Response<CheckInResponse> response) {
                       super.onResponse(call, response);
                        Log.d("API", "CheckIn API response code : " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess()) {
                                Log.d("API", "CheckIn API : " + response.code() + " : " + response.body().getMessage());
                                listener.onSuccess(response.body());
                            } else {
                                Log.d("API", "CheckIn API onResponse: " + response.code() + " : " + response.body().getMessage());
                                listener.onFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            Log.d("API", "CheckIn API onResponse : " + response.code() + " : " + response.message());
                            listener.onFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<CheckInResponse> call, @NotNull Throwable t) {
                       super.onFailure(call, t);
                        Log.d("API", "CheckIn API failure : " + t.getMessage());
                        listener.onFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void publishRide(String rideId, RidesListeners.OnPublishRideAPIFinishedListener listener) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .publishRide(REUtils.getJwtToken(), rideId)
                .enqueue(new BaseCallback<RideResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<RideResponse> call, @NotNull Response<RideResponse> response) {
                      super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess() && response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {
                                listener.onPublishRideSuccess();
                            } else if (response.body().getCode() == REConstants.API_UNAUTHORIZED) {
                                REUtils.navigateToLogin();
                            } else {
                                if (response.message().equalsIgnoreCase("Ok")) {
                                    listener.onPublishRideFailure("Already Published");
                                } else {
                                    listener.onPublishRideFailure(response.message());
                                }
                            }
                        } else {
                            listener.onPublishRideFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RideResponse> call, @NotNull Throwable t) {
                        super.onFailure(call,t);
                        listener.onPublishRideFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void startRide(String rideId, RidesListeners.OnUpdateRideStatusAPIFinishedListener listener) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .startRide(REUtils.getJwtToken(), rideId)
                .enqueue(new BaseCallback<RideResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<RideResponse> call, @NotNull Response<RideResponse> response) {
                       super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess() && response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {
                                listener.onUpdateRideStatusSuccess();
                            } else if (response.body().getCode() == REConstants.API_UNAUTHORIZED) {
                                REUtils.navigateToLogin();
                            } else {
                                listener.onUpdateRideStatusFailure(response.body().getMessage());
                            }
                        } else {
                            listener.onUpdateRideStatusFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RideResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        listener.onUpdateRideStatusFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void endRide(String rideId, RidesListeners.OnUpdateRideStatusAPIFinishedListener listener) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .endRide(REUtils.getJwtToken(), rideId)
                .enqueue(new BaseCallback<RideResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<RideResponse> call, @NotNull Response<RideResponse> response) {
                       super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess() && response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {
                                listener.onUpdateRideStatusSuccess();
                            } else if (response.body().getCode() == REConstants.API_UNAUTHORIZED) {
                                REUtils.navigateToLogin();
                            } else {
                                listener.onUpdateRideStatusFailure(response.body().getMessage());
                            }
                        } else {
                            listener.onUpdateRideStatusFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RideResponse> call, @NotNull Throwable t) {
                        super.onFailure(call,t);
                        listener.onUpdateRideStatusFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

/*

    @Override
    public void getDealerRideList(DealerRideRequest dealerRideRequest, final OnRidesResponseListener ridesResponseListener) {

        //TODO Below line code will removed once actual api integration will be done.
        Gson g = new Gson();
        final DealerRideResponse marqueeRidesResponse = g.fromJson(REUtils.loadJSONFromAsset(REApplication.getAppContext(), "dealer_rides_response.json"), DealerRideResponse.class);

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .getDealerRideList(jwtToken, dealerRideRequest)
                .enqueue(new Callback<DealerRideResponse>() {
                    @Override
                    public void onResponse(Call<DealerRideResponse> call, Response<DealerRideResponse> response) {
                        Log.d("API", "" + "dealer ride response code : " + response.code() + "  response : " + new Gson().toJson(response.body()));
                        //TODO will be removed below 2 lines
                        REUserModelStore.getInstance().setDealerRideList(marqueeRidesResponse.getData());
                        ridesResponseListener.onDealerRideResponse();

                    }

                    @Override
                    public void onFailure(Call<DealerRideResponse> call, Throwable t) {
                        Log.d("API", "dealer ride api failure : " + t.getMessage());
//                        ridesResponseListener.onDealerRideFailure(t.getMessage());
                        //TODO below code will be removed  below 2 lines
                        REUserModelStore.getInstance().setDealerRideList(marqueeRidesResponse.getData());
                        ridesResponseListener.onDealerRideResponse();
                    }
                });
    }

    @Override
    public void getMarqueeList(MarqueeRideRequest marqueeRideRequest, final OnRidesResponseListener ridesResponseListener) {

        Gson g = new Gson();
        final MarqueeRidesResponse marqueeRidesResponse = g.fromJson(REUtils.loadJSONFromAsset(REApplication.getAppContext(), "marquee_response.json"), MarqueeRidesResponse.class);

        REApplication.getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .getMarqueeRidesList(marqueeRideRequest).enqueue(new Callback<List<MarqueeRidesResponse>>() {
            @Override
            public void onResponse(Call<List<MarqueeRidesResponse>> call, Response<List<MarqueeRidesResponse>> response) {

                Log.e(TAG, " MarqueeResponse success called");

                REUserModelStore.getInstance().setMarqueeList(marqueeRidesResponse);
                ridesResponseListener.onMarqueeResponseSuccess();
            }

            @Override
            public void onFailure(Call<List<MarqueeRidesResponse>> call, Throwable t) {
                Log.d("API", "getMarqueeRides failure : " + t.getMessage());
                REUserModelStore.getInstance().setMarqueeList(marqueeRidesResponse);
                ridesResponseListener.onMarqueeResponseSuccess();
            }
        });
    }

    @Override
    public void getPopularRides(PopularRideRequest popularRideRequest, final OnRidesResponseListener ridesResponseListener) {

        Gson g = new Gson();
        final PopularRidesResponse popularRidesResponse = g.fromJson(REUtils.loadJSONFromAsset(REApplication.getAppContext(), "popular_response.json"), PopularRidesResponse.class);

        REApplication.getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .getPopularRidesList(popularRideRequest).enqueue(new Callback<List<PopularRidesResponse>>() {
            @Override
            public void onResponse(Call<List<PopularRidesResponse>> call, Response<List<PopularRidesResponse>> response) {

                Log.e(TAG, " PopularResponse success called");

                REUserModelStore.getInstance().setPopularRidesResponse(popularRidesResponse);
                ridesResponseListener.onPopularResponseSuccess();
            }

            @Override
            public void onFailure(Call<List<PopularRidesResponse>> call, Throwable t) {
                Log.d("API", "getPopularRides failure : " + t.getMessage());

                REUserModelStore.getInstance().setPopularRidesResponse(popularRidesResponse);
                ridesResponseListener.onPopularResponseSuccess();
            }
        });
    }
*/

    private String getJwtToken() {
        if (REApplication.getInstance().getUserLoginDetails() != null) {
            jwtToken = REApplication.getInstance().getUserTokenDetails();
        }
        return jwtToken;
    }
}
