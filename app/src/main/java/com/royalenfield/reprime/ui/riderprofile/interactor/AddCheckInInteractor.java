package com.royalenfield.reprime.ui.riderprofile.interactor;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.rides.AddCheckInRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.checkin.AddCheckInResponse;
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

public class AddCheckInInteractor implements ICheckINRideCreator.CheckInRideDetailsIntractor {

    @Override
    public void checkInRideDetails(AddCheckInRequest checkInRequest, String filePath, final CheckInRideDetailsListener checkInDetailsIntractor) {

        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("checkInPlaceName", checkInRequest.getCheckInPlaceName())
                .addFormDataPart("checkInDescription", String.valueOf(checkInRequest.getCheckInDescription()))
                .addFormDataPart("checkInCategory", checkInRequest.getCheckInCategory())
                .addFormDataPart("rideId", checkInRequest.getRideId())
                .addFormDataPart("address", checkInRequest.getAddress());

        //Add images in createride api
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            // Create a request body with file and image media type
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            multipartBody.addFormDataPart("checkInImage", filePath, fileReqBody);
        }

        RequestBody requestBody = multipartBody.build();

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .addCheckIn(REUtils.getJwtToken(), requestBody)
                .enqueue(new BaseCallback<AddCheckInResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<AddCheckInResponse> call, @NotNull Response<AddCheckInResponse> response) {
                       super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().isSuccess()) {
                                checkInDetailsIntractor.onCheckInRideDetailsSuccess();
                            } else if (response.body().getCode() == REConstants.API_UNAUTHORIZED) {
                                REUtils.navigateToLogin();
                            } else if (response.body() != null) {
                                checkInDetailsIntractor.onCheckInRideDetailsFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            // error case
                            checkInDetailsIntractor.onCheckInRideDetailsFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<AddCheckInResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        checkInDetailsIntractor.onCheckInRideDetailsFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}