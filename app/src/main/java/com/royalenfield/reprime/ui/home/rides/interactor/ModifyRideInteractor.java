package com.royalenfield.reprime.ui.home.rides.interactor;

import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.createride.CreateRideResponse;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.riderprofile.interactor.IModifyRideCreator;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class ModifyRideInteractor implements IModifyRideCreator.ModifyRideDetailsIntractor {

    @Override
    public void modifyRideDetails(CreateRideRequest createRideRequest, String filePath, final ModifyRideDetailsListener modifyRideListener) {

        String request = new Gson().toJson(createRideRequest.getWaypoints());
        String req_start = new Gson().toJson(createRideRequest.getStartPointCoordinates());
        String req_end = new Gson().toJson(createRideRequest.getEndPointCoordinates());
        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("rideId", createRideRequest.getRideId())
                .addFormDataPart("rideName", String.valueOf(createRideRequest.getRideName()))
                .addFormDataPart("startPoint", createRideRequest.getStartPoint())
                .addFormDataPart("startPointCoordinates", req_start)
                .addFormDataPart("endPoint", createRideRequest.getDestination())
                .addFormDataPart("endPointCoordinates", req_end)
                .addFormDataPart("waypoints", String.valueOf(request))
                .addFormDataPart("startDate", String.valueOf(createRideRequest.getStartDate()))
                .addFormDataPart("endDate", String.valueOf(createRideRequest.getEndDate()))
                .addFormDataPart("difficulty", String.valueOf(createRideRequest.getDifficulty()))
//                .addFormDataPart("rideStatus", String.valueOf(createRideRequest.getRideStatus()))
                .addFormDataPart("durationInDays", String.valueOf(createRideRequest.getDurationInDays()))
                .addFormDataPart("totalDistance", String.valueOf(createRideRequest.getTotalDistance()))
                .addFormDataPart("rideDetails", String.valueOf(createRideRequest.getRideDetails()))
                .addFormDataPart("terrainType", String.valueOf(createRideRequest.getTerrainType()))
                .addFormDataPart("startTime", String.valueOf(createRideRequest.getStartTime()))
                .addFormDataPart("endTime", String.valueOf(createRideRequest.getEndTime()))
                .addFormDataPart("rideType", String.valueOf(createRideRequest.getRideType()))
                .addFormDataPart("rideCategory", String.valueOf(createRideRequest.getRideCategory()));

        //Add images in createride api
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            // Create a request body with file and image media type
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            multipartBody.addFormDataPart("rideImages", filePath, fileReqBody);
        }

        RequestBody requestBody = multipartBody.build();

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .modifyRide(REUtils.getJwtToken(), requestBody)
                .enqueue(new BaseCallback<CreateRideResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<CreateRideResponse> call, @NotNull Response<CreateRideResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess()) {
                                modifyRideListener.onModifyRideDetailsSuccess();
                            } else if (response.body().getCode() == REConstants.API_UNAUTHORIZED) {
                                REUtils.navigateToLogin();
                            } else {
                                modifyRideListener.onModifyRideDetailsFailure(response.body().getMessage());
                            }
                        } else {
                            // error case
                            modifyRideListener.onModifyRideDetailsFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<CreateRideResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        modifyRideListener.onModifyRideDetailsFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}
