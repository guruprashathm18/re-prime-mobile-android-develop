package com.royalenfield.reprime.ui.home.homescreen.interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.vehicledetails.VehicleDetailRequest;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailResponseFirestore;
import com.royalenfield.reprime.models.response.web.firebasetoken.CustomTokenResponse;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleDetailResponse;
import com.royalenfield.reprime.ui.home.homescreen.listener.FirebaseCustomTokenListener;
import com.royalenfield.reprime.ui.home.homescreen.listener.OnVehicleDetailsFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.generateLogs;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;

public class HomeActivityInteractor implements IHomeActivityInteractor {

    @Override
    public void getVehicleDetails( OnVehicleDetailsFinishedListener onVehicleDetailsFinishedListener) {
        String accessToken = REApplication.getInstance()
                .getUserTokenDetails();
        VehicleDetailRequest vehicleDetailRequest = new VehicleDetailRequest();
        if(REApplication.getInstance().getUserLoginDetails()!=null&&REApplication.getInstance().getUserLoginDetails().getData()!=null) {
            vehicleDetailRequest.setAppId(REConstants.APP_ID);
            vehicleDetailRequest.setGuid(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
            vehicleDetailRequest.setLoggedInUserMobileNo(REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone());
            vehicleDetailRequest.setLoggedInUserEmail(REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
            vehicleDetailRequest.setLoggedInUserName(REApplication.getInstance().getUserLoginDetails().getData().getUser().getFirstName() + " " + REApplication.getInstance().getUserLoginDetails().getData().getUser().getLastName());
            REApplication
                    .getInstance()
                    .getREWebsiteApiInstance()
                    .getWebsiteAPI()
                    .getVehicleDetails(REConstants.APP_ID, accessToken, vehicleDetailRequest)
                    .enqueue(new BaseCallback<VehicleDetailResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<VehicleDetailResponse> call, @NonNull Response<VehicleDetailResponse> response) {
                            super.onResponse(call, response);
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().
                                        getCode() == REConstants.API_SUCCESS_CODE) {
                                    if (response.body().getCode() == REConstants.API_SUCCESS_CODE) {
                                        updateFirestoreVehicleFlag();
                                        onVehicleDetailsFinishedListener.onVehicleDetailSuccess(response.body().getData().getVehicleInfo());
                                    } else {
                                        onVehicleDetailsFinishedListener.onVehicleDetailFail(response.body().getCode());
                                    }
                                } else if (response.body().getCode() == REConstants.API_SUCCESS_NO_DATA_CODE) {
                                    updateFirestoreVehicleFlag();
                                    onVehicleDetailsFinishedListener.onVehicleDetailSuccess(null);
                                } else {
                                    onVehicleDetailsFinishedListener.onVehicleDetailFail(response.body().getCode());
                                }
                            } else {
                                // error case
                                onVehicleDetailsFinishedListener.onVehicleDetailFail(response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<VehicleDetailResponse> call, @NonNull Throwable t) {
                            super.onFailure(call, t);
                            onVehicleDetailsFinishedListener.onVehicleDetailFail(0);
                        }
                    });
        }
        else{
            onVehicleDetailsFinishedListener.onVehicleDetailFail(0);
        }
    }

    @Override
    public void getVehicleDetailsFromFirestore(OnVehicleDetailsFinishedListener onVehicleDetailsFinishedListener) {
        if(REApplication.getInstance().getUserLoginDetails()!=null&&REApplication.getInstance().getUserLoginDetails().getData()!=null) {
            REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document(REFirestoreConstants.VEHICLE_DOCUMENT)
                    .get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(documentSnapshot.getData());
                    VehicleDetailResponseFirestore vehicleDetailResponseFirestore = gson.fromJson(jsonElement, VehicleDetailResponseFirestore.class);
                    onVehicleDetailsFinishedListener.onVehicleDetailSuccessFireStore(vehicleDetailResponseFirestore);
                } else {
                    onVehicleDetailsFinishedListener.onVehicleDetailSuccessFireStore(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    generateLogs(null, REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId() + "/" + REFirestoreConstants.VEHICLE_DOCUMENT, e.getMessage());
                    onVehicleDetailsFinishedListener.onVehicleDetailSuccessFireStore(null);
                }
            });
        }
        else{
            onVehicleDetailsFinishedListener.onVehicleDetailFail(0);
        }
    }

    private void updateFirestoreVehicleFlag() {
//        Map<String, Object> popupFlags = new HashMap<>();
//        popupFlags.put(REConstants.GET_VEHICLE_DETAIL_FLAG, true);
      //  FirestoreManager.getInstance().updateUserSettingFirebaseField(popupFlags);
    }

    @Override
    public void getFirebaseCustomToken(FirebaseCustomTokenListener firebaseCustomTokenListener) {
        if(REApplication.getInstance().mFirebaseAuth!=null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser()!=null){
            if( REApplication.getInstance().mFirebaseAuth.getCurrentUser().getUid().equalsIgnoreCase(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())){
                firebaseCustomTokenListener.onFirebaseAuthSuccess();
            }
            else {
                getCustomTokenData(firebaseCustomTokenListener);
            }
        }
        else
            getCustomTokenData(firebaseCustomTokenListener);
    }

    private void getCustomTokenData(FirebaseCustomTokenListener firebaseCustomTokenListener) {
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getFirebaseTokenApi()
                .getFirebaseToken()
                .enqueue(new BaseCallback<CustomTokenResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CustomTokenResponse> call, @NonNull Response<CustomTokenResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {
                                if (response.body().getCode() == REConstants.API_SUCCESS_CODE) {
                                    firebaseCustomTokenListener.onFirebaseCustomTokenSuccess(response.body().getData(),response.body().getRequestId());
                                } else {
                                    firebaseCustomTokenListener.onFirebaseCustomFailure(response.body().getMessage());
                                }
                            }
                            else {
                                firebaseCustomTokenListener.onFirebaseCustomFailure(response.body().getMessage());
                            }
                        } else {
                            // error case
                            firebaseCustomTokenListener.onFirebaseCustomFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CustomTokenResponse> call, @NonNull Throwable t) {
                        super.onFailure(call, t);
                        firebaseCustomTokenListener.onFirebaseCustomFailure("");
                    }
                });

    }
}
