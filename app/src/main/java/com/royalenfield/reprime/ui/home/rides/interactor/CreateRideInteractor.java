package com.royalenfield.reprime.ui.home.rides.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.createride.CreateRideResponse;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;

public class CreateRideInteractor implements ICreateRideInteractor {

    private String TAG = CreateRideInteractor.class.getSimpleName();

    @Override
    public void createRide(CreateRideRequest createRideRequest, String filePath, final RidesListeners.IOnCreateRideFinishedListener createRideFinishedListener) {

        String request = new Gson().toJson(createRideRequest.getWaypoints());
        String req_start = new Gson().toJson(createRideRequest.getStartPointCoordinates());
        String req_end = new Gson().toJson(createRideRequest.getEndPointCoordinates());
        String req_hashtags = new Gson().toJson(createRideRequest.getHashTags());
        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
//                .addFormDataPart("createdBy", createRideRequest.getCreatedBy())
                .addFormDataPart("startPointCoordinates", req_start)
                .addFormDataPart("endPointCoordinates", req_end)
                .addFormDataPart("endPoint", createRideRequest.getEndPoint())
                .addFormDataPart("startPoint", createRideRequest.getStartPoint())
                .addFormDataPart("waypoints", String.valueOf(request))
                .addFormDataPart("rideName", String.valueOf(createRideRequest.getRideName()))
                .addFormDataPart("difficulty", String.valueOf(createRideRequest.getDifficulty()))
                .addFormDataPart("startDate", String.valueOf(createRideRequest.getStartDate()))
                .addFormDataPart("endDate", String.valueOf(createRideRequest.getEndDate()))
                .addFormDataPart("durationInDays", String.valueOf(createRideRequest.getDurationInDays()))
                .addFormDataPart("totalDistance", String.valueOf(createRideRequest.getTotalDistance()))
                .addFormDataPart("rideDetails", String.valueOf(createRideRequest.getRideDetails()))
                .addFormDataPart("terrainType", String.valueOf(createRideRequest.getTerrainType()))
                .addFormDataPart("startTime", String.valueOf(createRideRequest.getStartTime()))
                .addFormDataPart("endTime", String.valueOf(createRideRequest.getEndTime()))
//                .addFormDataPart("rideStatus", String.valueOf(createRideRequest.getRideStatus()))
                .addFormDataPart("rideCategory", String.valueOf(createRideRequest.getRideCategory()))
                .addFormDataPart("rideType", String.valueOf(createRideRequest.getRideType()))
                .addFormDataPart("hashTags", req_hashtags);

        //Add images in createride api
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            // Create a request body with file and image media type
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            multipartBody.addFormDataPart("rideImages", filePath, fileReqBody);
        }

        RequestBody requestBody = multipartBody.build();

        Log.e("test", "jwt token  =" + REUtils.getJwtToken());
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .createRide(REUtils.getJwtToken(), requestBody)
                .enqueue(new BaseCallback<CreateRideResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<CreateRideResponse> call, @NotNull Response<CreateRideResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess()) {
                                createRideFinishedListener.onSuccess();
                            } else if (response.body().getCode() == REConstants.API_UNAUTHORIZED) {
                                REUtils.navigateToLogin();
                            } else {
                                createRideFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            // error case
                            createRideFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<CreateRideResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        createRideFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}
