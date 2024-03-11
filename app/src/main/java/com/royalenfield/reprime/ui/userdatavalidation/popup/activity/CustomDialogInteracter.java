package com.royalenfield.reprime.ui.userdatavalidation.popup.activity;

import android.accounts.NetworkErrorException;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.realTimeDatabase.OnStateandCityResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.rest.web.DeleteUserContactResponse;
import com.royalenfield.reprime.rest.web.SwapApprovalResponse;
import com.royalenfield.reprime.rest.web.WebsiteNetworkServiceInterface;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.userdatavalidation.popup.presenter.UserContactResponseCallback;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.AddAddressRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.AddContactRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.UpdateContactRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.AddAddressResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.presenter.MasterDataCallback;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.AddContactResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UpdateContactResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UserContactDetailResponseModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.generateLogs;
import static com.royalenfield.reprime.utils.REFirestoreConstants.REALTIME_DB;

public class CustomDialogInteracter {


    private final MasterDataCallback mCallback;

    public CustomDialogInteracter(MasterDataCallback callback) {
        mCallback = callback;
    }

    public void getStateMasterFirestore(String country){
        if(mCallback!=null){
            FirestoreManager.getInstance().getStateMasterFirestore(country,mCallback);
        }
    }

    public void getCityMasterFirestore(String cityCode,String countryCode){
        if(mCallback!=null){
            FirestoreManager.getInstance().getCityMasterFirestore(countryCode,cityCode,mCallback);
        }
    }

    public void updateAddress(String type, UpdateAddressRequestModel updateAddressrequestModel, boolean isAdd, OnUpdateContactFinishedListener listener) {
        WebsiteNetworkServiceInterface networkServiceInterface = REApplication.getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI();

        Call<UpdateAddressResponseModel> updateAddressResponseModelCall = null;
        if (isAdd) {
            updateAddressResponseModelCall = networkServiceInterface.addAddressDetail(updateAddressrequestModel);
        } else {
            updateAddressResponseModelCall = networkServiceInterface.updateAddress(updateAddressrequestModel);
        }
        updateAddressResponseModelCall
                .enqueue(new BaseCallback<UpdateAddressResponseModel>() {
                    @Override
                    public void onResponse(Call<UpdateAddressResponseModel> call
                            , Response<UpdateAddressResponseModel> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {
                            listener.onUpdateAddressSuccess(response.body(), type, updateAddressrequestModel);
                        } else {
                            if(response.body() != null)
                                listener.onUpdateAddressFail(response.body().getMessage(), type);
                            else
                                listener.onUpdateAddressFail(response.message(), type);
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateAddressResponseModel> call, Throwable t) {
                        super.onFailure(call, t);
                        if (t instanceof NetworkErrorException) {
                            listener.onUpdateAddressFail(REApplication.getAppContext().
                                    getResources().getString(R.string.network_failure), type);
                        } else {
                            listener.onUpdateAddressFail(t.getMessage(), type);
                        }
                    }
                });
    }

    public void updateContact(String updateType, UpdateContactRequestModel updateContactRequestModel, boolean isAdd
            , OnUpdateContactFinishedListener listener) {
        WebsiteNetworkServiceInterface websiteNetworkServiceInterface = REApplication.getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI();

        Call<UpdateContactResponseModel> updateContactResponseModelCall = null;
        if (isAdd) {
            updateContactResponseModelCall = websiteNetworkServiceInterface.addContact(updateContactRequestModel.getCallingCode()
                    , updateContactRequestModel.getSecondary()
                    , updateContactRequestModel.getEmergency());
        } else {
            updateContactResponseModelCall = websiteNetworkServiceInterface.updateContact(updateContactRequestModel.getCallingCode()
                    , updateContactRequestModel.getSecondary()
                    , updateContactRequestModel.getEmergency()
                    , updateContactRequestModel.getEmail());
        }


        updateContactResponseModelCall.enqueue(new BaseCallback<UpdateContactResponseModel>() {
            @Override
            public void onResponse(Call<UpdateContactResponseModel> call
                    , Response<UpdateContactResponseModel> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {
                    listener.onUpdateContactSuccess(response.body(), updateType, updateContactRequestModel);
                } else {
                    if(response.body() != null)
                    listener.onUpdateContactFail(response.body().getMessage(), updateType);
                    else
                        listener.onUpdateContactFail(response.message(), updateType);

                }
            }

            @Override
            public void onFailure(Call<UpdateContactResponseModel> call, Throwable t) {
                super.onFailure(call, t);
                if (t instanceof NetworkErrorException) {
                    listener.onUpdateContactFail(REApplication.getAppContext().
                            getResources().getString(R.string.network_failure), updateType);
                } else {
                    listener.onUpdateContactFail(t.getMessage(), updateType);
                }
            }
        });
    }

    public void getUserContactDetails(UserContactResponseCallback callback) {
if(REUserModelStore.getInstance().getProfileData()!=null&&REUserModelStore.getInstance().getProfileData().getContactDetails()!=null)
        callback.onUserContactSuccess(REUserModelStore.getInstance().getProfileData().getContactDetails());
    }

    public void deleteSecondaryNum(OnUpdateContactFinishedListener listener) {

        REApplication.getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .deleteSecondaryPhoneNum()
                .enqueue(new BaseCallback<DeleteUserContactResponse>() {
                    @Override
                    public void onResponse(Call<DeleteUserContactResponse> call
                            , Response<DeleteUserContactResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess() ) {
                            listener.onDeleteSecondarySuccess();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteUserContactResponse> call, Throwable t) {
                        super.onFailure(call, t);
                            listener.onDeleteSecondaryFail();
                    }
                });
    }

    public void checkIfNumIsSwappable(String secondaryPhone, String callingCode, OnUpdateContactFinishedListener listener) {
        REApplication.getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .checkIfNumIsSwappable(secondaryPhone, callingCode.replaceAll("[-+^:,]", ""))
                .enqueue(new BaseCallback<SwapApprovalResponse>() {
                    @Override
                    public void onResponse(Call<SwapApprovalResponse> call
                            , Response<SwapApprovalResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {
                            listener.onSwapApprovalSuccess();
                        } else {
                            if(response.body()!=null)
                            listener.onSwapApprovalFail( REApplication.getAppContext().getResources().getString(R.string.error_not_swapble));
                            else
                                listener.onSwapApprovalFail(REApplication.getAppContext().getResources().getString(R.string.system_down));
                        }
                    }

                    @Override
                    public void onFailure(Call<SwapApprovalResponse> call, Throwable t) {
                        super.onFailure(call, t);
                            listener.onSwapApprovalFail(t.getMessage());
                    }
                });
    }
}
