package com.royalenfield.reprime.ui.home.rides.interactor;

import android.util.Log;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.joinride.JoinRideRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.joinride.JoinRideResponse;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinRideInteractor implements IJoinRideInteractor {

    @Override
    public void joinRide(String rideType, String rideId, RidesListeners.OnAPICallFinishedListener listener) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .joinRide(REUtils.getJwtToken(),
                        new JoinRideRequest(rideType, rideId))
                .enqueue(new BaseCallback<JoinRideResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<JoinRideResponse> call, @NotNull Response<JoinRideResponse> response) {
                       super.onResponse(call, response);
                        Log.d("API", "JoinRide response code : " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess()) {
                                Log.d("API", "JoinRide : " + response.code() + " : " + response.body().getMessage());
                                listener.onSuccess();
                            } else if (response.body().getCode() == REConstants.API_UNAUTHORIZED) {
                                REUtils.navigateToLogin();
                            } else {
                                Log.d("API", "JoinRide  onResponse: " + response.code() + " : " + response.body().getMessage());
                                listener.onFailure(response.body().getMessage());
                            }
                        } else {
                            Log.d("API", "JoinRide onResponse : " + response.code() + " : " + response.message());
                            listener.onFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<JoinRideResponse> call, @NotNull Throwable t) {
                       super.onFailure(call, t);
                        Log.d("API", "JoinRide failure : " + t.getMessage());
                        listener.onFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}
