package com.royalenfield.firebase.fireStore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.remoteconfig.internal.DefaultsXmlParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.royalenfield.bluetooth.otap.listener.OnOTAPCallback;
import com.royalenfield.firestore.userinfo.UserInfoResponse;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.proxy.service.vehicledetails.VehicleDetailsRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.models.response.firestore.ourworld.NewsResponse;
import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.UserUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.GetVehicleDetailsResponse;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.UserVehicle;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailResponseFirestore;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailsResponse;
import com.royalenfield.reprime.models.response.remoteconfig.RemoteConfigData;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;
import com.royalenfield.reprime.models.response.web.signup.RequestConsent;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.listener.OnRealtimeVehicledetailListner;
import com.royalenfield.reprime.ui.home.homescreen.presenter.HomeActivityPresenter;
import com.royalenfield.reprime.ui.home.ourworld.views.OurWorldView;
import com.royalenfield.reprime.ui.home.rides.views.RidesHomeView;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnEditProfileFinishedListener;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnUserInfoFirebaseListener;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.onboarding.signup.listeners.OnConsentDataListener;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryListModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.presenter.MasterDataCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.generateLogs;
import static com.royalenfield.reprime.utils.REConstants.APP_VERSION_DATA;
import static com.royalenfield.reprime.utils.REConstants.COUNTRY_URL_KEY;
import static com.royalenfield.reprime.utils.REConstants.REMOTE_DATA;
import static com.royalenfield.reprime.utils.REConstants.REMOTE_VERSION;
import static com.royalenfield.reprime.utils.REFirestoreConstants.CITY_REALTIME_DB;
import static com.royalenfield.reprime.utils.REFirestoreConstants.REALTIME_DB;
import static com.royalenfield.reprime.utils.REFirestoreConstants.REMOTE_CONFIG;
import static com.royalenfield.reprime.utils.REFirestoreConstants.STATE_REALTIME_DB;
import static com.royalenfield.reprime.utils.REFirestoreConstants.STATIC_REALTIME_DB;
import static com.royalenfield.reprime.utils.REFirestoreConstants.APP_SECRETS;
import static com.royalenfield.reprime.utils.REFirestoreConstants.CONNECTED_PROVISIONING;

/**
 * Firestore Manager class for getting data from firestore
 */
public class FirestoreManager {

    private static final String TAG = FirestoreManager.class.getSimpleName();
    private static FirestoreManager mFirestoreInstance;
    private List<VehicleDetailsResponse> mVehicleDetailsList = new ArrayList<>();
    private OnFirestoreVehicleListCallback mOnFireStoreVehicleDetailsCallback;
    private String mUserMobileNo;
    private int count = 0;
    private ListenerRegistration mPendingRideEventListener, mUserInfoEventListener, mUpcomingEventListener, mPastRideEventListener,
            mOngoingEventListener, mRejectedEventListener, mDealerUpcomingEventListener, mPopularEventListener,
            mMarqueeEventListener, mUserUpcomingEventListener, mNewsEventListener, mEventsEventListener, mVehicleDetailsEventListener,
            mServiceHistoryListener, mrealTimeVehicelDetailListener, mOTAPEventListener,mUserConsentListener,mAlertTimeListener,mRealtimeProfileListener;
    private Query currentDealerRidesQuery = null;
    private Query currentUserRidesQuery = null;
    private DocumentSnapshot lastVisibleUserRides = null;
    private DocumentSnapshot lastVisibleDealerRides = null;

    /**
     * Returns the FirebaseFirestore application instance.
     *
     * @return {@link FirebaseFirestore} instance.
     */
    public static FirestoreManager getInstance() {
        if (mFirestoreInstance == null) {
            mFirestoreInstance = new FirestoreManager();
        }
        return mFirestoreInstance;
    }


    /**
     * Fetches the userInfo from firestore after login/verifyaccount
     */
    public void getUserInfo() {
        if (mUserMobileNo != null && !mUserMobileNo.isEmpty()) {
            DocumentReference query = FirebaseFirestore.getInstance().collection(mUserMobileNo).document(REFirestoreConstants.USER_INFO_COLLECTION);
            mUserInfoEventListener = query.addSnapshotListener((documentSnapshot, e) -> {
                if (e == null && documentSnapshot != null && documentSnapshot.getData() != null &&
                        documentSnapshot.getData().size() > 0) {
                    try {
                        REUserModelStore.getInstance().
                                setUserInfoResponse(documentSnapshot.toObject(UserInfoResponse.class));
                        setDataToUserInfo();
                    } catch (RuntimeException exception) {
                        RELog.e(exception);
                    }
                }
            });
        }
    }

    /**
     * Fetches the userInfo from firestore after login/verifyaccount
     */
    public void getUserProfileData(OnEditProfileFinishedListener listener) {
        DocumentReference query = FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(REFirestoreConstants.USER_INFO_COLLECTION);
        mUserInfoEventListener = query.addSnapshotListener((documentSnapshot, e) -> {
            if (e == null && documentSnapshot != null && documentSnapshot.getData() != null &&
                    documentSnapshot.getData().size() > 0) {
                try {
                    storeProfileDataInModelStore(documentSnapshot.toObject(ProfileData.class));
                    Log.e("SUCCESS PROFILE", documentSnapshot.toString());
                    if (listener != null)
                        listener.onGetProfileDetailsSuccess();
					else {
						Intent profileUpdate = new Intent(REConstants.FIRESTORE_PROFILE);
						LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(profileUpdate);
					}
                } catch (RuntimeException exception) {
                    RELog.e(exception);
                }
            }
        });

    }

	public void getUserProfileDataListener(OnUserInfoFirebaseListener listener) {

		final DocumentReference vehicelDocRef = REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document(REFirestoreConstants.USER_INFO_COLLECTION);
		if(mRealtimeProfileListener!=null)
			mRealtimeProfileListener.remove();
		mRealtimeProfileListener = vehicelDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
			@Override
			public void onEvent(@Nullable DocumentSnapshot snapshot,
								@Nullable FirebaseFirestoreException e) {
				if (e != null) {

					RELog.d(TAG, e.getMessage()+"Listen failed.");
					return;
				}
				if (snapshot != null && snapshot.exists()) {
					Gson gson = new Gson();
					JsonElement jsonElement = gson.toJsonTree(snapshot.getData());
					try {
						ProfileData profileData = gson.fromJson(jsonElement, ProfileData.class);

						storeProfileDataInModelStore(profileData);
						if (listener != null)
							listener.onGetProfileDetailsSuccess(profileData);
					} catch (RuntimeException exception) {
						RELog.e(exception);
					}
				}
			}
		});



		DocumentReference query = FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(REFirestoreConstants.USER_INFO_COLLECTION);
		mUserInfoEventListener = query.addSnapshotListener((documentSnapshot, e) -> {
			if (e == null && documentSnapshot != null && documentSnapshot.getData() != null &&
					documentSnapshot.getData().size() > 0) {

			}
		});

	}


	/**
     * Store user profile data into Singleton {@link REUserModelStore} object.
     *
     * @param profileData {@link ProfileData} object.
     */
    private void storeProfileDataInModelStore(ProfileData profileData) {
        if (profileData != null) {
//			if(profileData.getAccountStatus()!=null)
//				if(!profileData.getAccountStatus().getActive())
//					REBaseActivity.isUserActive=false;
//			else
//					REBaseActivity.isUserActive=true;
			REUserModelStore.getInstance().setEmail(profileData.getContactDetails().getEmail());
            REUserModelStore.getInstance().setProfileData(profileData);
            REUserModelStore.getInstance().setProfileUrl(profileData.getProfileUrl());
        }
    }

    /**
     * This method gets the payment status
     *
     * @param paymentCallback : OnPaymentCallback
     */
    public void getPaymentStatus(OnPaymentCallback paymentCallback) {
        FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(REFirestoreConstants.SERVICE_PAYMENT).
                addSnapshotListener((documentSnapshot, e) -> {
                    if (e == null && documentSnapshot != null && documentSnapshot.getData() != null &&
                            documentSnapshot.getData().size() > 0) {
                        try {
                            Map<String, Object> map = documentSnapshot.getData();
                            if (map != null && map.size() > 0 && paymentCallback != null) {
                                paymentCallback.onPaymentStatus(String.valueOf(map.
                                        get(REFirestoreConstants.SERVICE_PAYMENT_STATUS)), String.valueOf(map.
                                        get(REFirestoreConstants.SERVICE_PAYMENT_CASEID)));
                            }
                        } catch (Exception exception) {
                            if (paymentCallback != null) {
                                paymentCallback.onPaymentStatus("", "");
                            }
                            RELog.e(exception);
                        }
                    } else {
                        if (paymentCallback != null) {
                            paymentCallback.onPaymentStatus("", "");
                        }
                        if (e != null)
                            generateLogs(null, REUtils.getUserId() + "/" + REFirestoreConstants.SERVICE_PAYMENT, e.getMessage());
                    }
                });
    }

    /**
     * Sets the user data to user object
     */
    private void setDataToUserInfo() {
        REUserModelStore.getInstance().setAboutMe(REUserModelStore.getInstance().getUserInfoResponse().getAboutMe());
        REUserModelStore.getInstance().setDob(REUserModelStore.getInstance().getUserInfoResponse().getDob());
        REUserModelStore.getInstance().setGender(REUserModelStore.getInstance().getUserInfoResponse().getGender());
        REUserModelStore.getInstance().setProfileUrl(REUserModelStore.getInstance().getUserInfoResponse().getProfileUrl());
        REUserModelStore.getInstance().setCity(REUserModelStore.getInstance().getUserInfoResponse().getCity());
    }

    /**
     * Fetches the vehicle details from Firestore
     *
     * @param onFirestoreVehicleListCallback :onFirestoreVehicleListCallback
     */
    public void getVehicleDetails(OnFirestoreVehicleListCallback onFirestoreVehicleListCallback) {
        mUserMobileNo = REApplication.getInstance().getUserLoginDetails().getData().
                getUser().getPhone();
        mOnFireStoreVehicleDetailsCallback = onFirestoreVehicleListCallback;
        mVehicleDetailsList.clear();
        mOnFireStoreVehicleDetailsCallback = onFirestoreVehicleListCallback;
        if (mUserMobileNo != null && !mUserMobileNo.isEmpty()) {
            Log.d("API", "Firestore called for vehicle details :" + mUserMobileNo);
            //This snapshot will always listen for the changes
            REApplication.mFireStoreInstance.collection(mUserMobileNo).
                    document(REFirestoreConstants.VEHICLE_DETAILS_COLLECTION).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    //Checking the error value and then proceeding
                    if (e == null) {

                        REUserModelStore.getInstance().setVehicleDetailsList(null);
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Log.d("API", "Firestore Data Success");
                            Map<String, Object> map = documentSnapshot.getData();
                            if (map != null && map.size() > 0) {
                                REUserModelStore.getInstance().setVehicleDetailsList(getVehicleListObject(map));
                                onVehicleDetailsSuccess();
                                Intent firestoreUpdate = new Intent(REConstants.FIRESTORE_UPDATE);
                                LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(firestoreUpdate);
                            } else {
                                onVehicleDetailsSuccess();
                            }
                        } else {
                            Log.d("API", "Firestore vehicle data is null");
                            //Calling stored procedure API to push the data to firestore
                            if (count <= 1) {
                                pushDataToFirestore(mUserMobileNo, mOnFireStoreVehicleDetailsCallback);
                            } else {
                                onVehicleDetailsSuccess();
                            }
                        }
                    } else {
                        if (e != null)
                            generateLogs(null, REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId() + "/" + REFirestoreConstants.VEHICLE_DETAILS_COLLECTION, e.getMessage());

                        if (mOnFireStoreVehicleDetailsCallback != null)
                            mOnFireStoreVehicleDetailsCallback.onFirestoreVehicleDetailsFailure(REUtils.getErrorMessageFromCode(0));
                    }
                }
            });
        } else {
            //Showing error message if mobile no is unavailable.
            if (mOnFireStoreVehicleDetailsCallback != null)
                mOnFireStoreVehicleDetailsCallback.onFirestoreVehicleDetailsFailure(REUtils.getErrorMessageFromCode(0));
        }
    }

    private void onVehicleDetailsSuccess() {
        if (mOnFireStoreVehicleDetailsCallback != null) {
            // mOnFireStoreVehicleDetailsCallback.onFirestoreVehicleDetailsSuccess();
            mOnFireStoreVehicleDetailsCallback = null;
        }
    }

    /**
     * API call to stored procedure to push the user vehicle data to firestore
     *
     * @param mobileNo                           : String
     * @param mOnFireStoreVehicleDetailsCallback : OnFirestoreVehicleListCallback
     */
    public void pushDataToFirestore(String mobileNo, OnFirestoreVehicleListCallback
            mOnFireStoreVehicleDetailsCallback) {
        Log.d("API", "Get vehicle details API called:" + mobileNo);
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .getVehicleDetails(REApplication.getInstance()
                                .getUserTokenDetails(),
                        new VehicleDetailsRequest("", "", "", mobileNo))
                .enqueue(new BaseCallback<GetVehicleDetailsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GetVehicleDetailsResponse> call,
                                           @NonNull Response<GetVehicleDetailsResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "Get vehicle Details API Response: "
                                + response.code() + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == REConstants.API_SUCCESS_CODE
                                    && response.body().getResult().equals(REConstants.API_SUCCESS_RESULT)) {
                                //Fecthing from firestore
                                getVehicleDetails(mOnFireStoreVehicleDetailsCallback);
                                count++;
                                // Setting vehicle details sync value to true
                                try {
                                    REPreference.getInstance().putBoolean(REApplication.getAppContext(), REConstants.VEHICLE_DETAILS_SYNC, true);
                                } catch (PreferenceException e) {
                                    RELog.e(e);
                                }
                            } else {
                                mOnFireStoreVehicleDetailsCallback.
                                        onFirestoreVehicleDetailsFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            mOnFireStoreVehicleDetailsCallback.
                                    onFirestoreVehicleDetailsFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetVehicleDetailsResponse> call, @NonNull Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "Get vehicle details failure :" + t.getMessage());
                        mOnFireStoreVehicleDetailsCallback.
                                onFirestoreVehicleDetailsFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }


    /**
     * Fetches the Dealer upcoming rides from Firestore
     */
    public void getDealerUpcomingRides(RidesHomeView mRidesHomeView) {
        if (currentDealerRidesQuery == null) {
            currentDealerRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.DEALER_UPCOMING_RIDES)
                    .orderBy("createdOn", Query.Direction.DESCENDING)
                    .limit(25);
        }
        Task<QuerySnapshot> mUserUpcomingEventListenernew = currentDealerRidesQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                    try {
                        lastVisibleDealerRides = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                        if (lastVisibleDealerRides != null) {
                            currentDealerRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.DEALER_UPCOMING_RIDES)
                                    .orderBy("createdOn", Query.Direction.DESCENDING)
                                    .startAfter(lastVisibleDealerRides)
                                    .limit(25);
                        }
                        List<DealerUpcomingRidesResponse> mDealerUpcomingDealerUpcomingRidesResponse = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Map<String, Object> map = snapshot.getData();
                            if (map != null) {
                                DealerUpcomingRidesResponse dealerUpcomingRidesResponse = new DealerUpcomingRidesResponse((String) map.get("_id"),
                                        (Map<String, Map<String, Object>>) map.get("dealerInfo"), (String) map.get("dealerName"),
                                        (String) map.get("endPoint"), (ArrayList<Map<String, String>>) map.get("rideImages"),
                                        (Map<String, Object>) map.get("rideInfo"), (String) map.get("rideName"),
                                        (ArrayList<Map<String, Object>>) map.get("ridersJoined"), (String) map.get("startPoint"),
                                        (ArrayList<Long>) map.get("startPointCoordinates"));
                                mDealerUpcomingDealerUpcomingRidesResponse.add(dealerUpcomingRidesResponse);
                            }
                        }
                        RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(null);
                        RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(mDealerUpcomingDealerUpcomingRidesResponse);
                        Log.d("Firestore", "DealerUpcomingRides success size 2 is :" + mDealerUpcomingDealerUpcomingRidesResponse.size());
                        mRidesHomeView.onDealerRideResponse();
                    } catch (Exception e) {
                        mRidesHomeView.onDealerRideFailure(REApplication.getAppContext().getResources().getString(R.string.text_dealer_rides_error));
                    }
                } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {

                    if (lastVisibleDealerRides == null) {
                        mRidesHomeView.onDealerRideFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_dealerrides));
                    }
                }
            }
        });
    }

    public void getDealerUpcomingRides1(RidesHomeView mRidesHomeView) {
        try {
            if (currentDealerRidesQuery == null) {
                currentDealerRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.DEALER_UPCOMING_RIDES)
                        .orderBy("createdOn", Query.Direction.DESCENDING)
                        .limit(25);
            }
//            currentDealerRidesQuery.get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot documentSnapshots) {
//
//                            if(documentSnapshots.getDocuments().size()>0) {
//                                // Get the last visible document
//                                lastVisibleDealerRides = documentSnapshots.getDocuments()
//                                        .get(documentSnapshots.size() - 1);
//                                if (lastVisibleDealerRides != null) {
//                                    currentDealerRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.DEALER_UPCOMING_RIDES)
//                                            .orderBy("createdOn")
//                                            .startAfter(lastVisibleDealerRides)
//                                            .limit(25);
//                                }
//                                List<DealerUpcomingRidesResponse> mDealerUpcomingDealerUpcomingRidesResponse = new ArrayList<>();
//                                for (DocumentSnapshot snapshot : documentSnapshots) {
//                                    Map<String, Object> map = snapshot.getData();
//                                    if (map != null) {
//                                        DealerUpcomingRidesResponse dealerUpcomingRidesResponse = new DealerUpcomingRidesResponse((String) map.get("_id"),
//                                                (Map<String, Map<String, Object>>) map.get("dealerInfo"), (String) map.get("dealerName"),
//                                                (String) map.get("endPoint"), (ArrayList<Map<String, String>>) map.get("rideImages"),
//                                                (Map<String, Object>) map.get("rideInfo"), (String) map.get("rideName"),
//                                                (ArrayList<Map<String, Object>>) map.get("ridersJoined"), (String) map.get("startPoint"),
//                                                (ArrayList<Long>) map.get("startPointCoordinates"));
//                                        mDealerUpcomingDealerUpcomingRidesResponse.add(dealerUpcomingRidesResponse);
//                                    }
//                                }
//                                RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(null);
//                                RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(mDealerUpcomingDealerUpcomingRidesResponse);
//                                Log.d("Firestore", "DealerUpcomingRides success size 2 is :" + mDealerUpcomingDealerUpcomingRidesResponse.size());
//                                mRidesHomeView.onDealerRideResponse();
//                            }
//                            else if (lastVisibleDealerRides==null)
//                            {
//                              mRidesHomeView.onDealerRideFailure(REApplication.getAppContext().getResources().getString(R.string.text_dealer_rides_error));
//                            }
//                            // Use the query for pagination
//                            // ...
//                        }
//                    });

            mDealerUpcomingEventListener = currentDealerRidesQuery.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e == null && queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                    try {
                        lastVisibleDealerRides = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                        if (lastVisibleDealerRides != null) {
                            currentDealerRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.DEALER_UPCOMING_RIDES)
                                    .orderBy("createdOn", Query.Direction.DESCENDING)
                                    .startAfter(lastVisibleDealerRides)
                                    .limit(25);
                        }
                        List<DealerUpcomingRidesResponse> mDealerUpcomingDealerUpcomingRidesResponse = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Map<String, Object> map = snapshot.getData();
                            if (map != null) {
                                DealerUpcomingRidesResponse dealerUpcomingRidesResponse = new DealerUpcomingRidesResponse((String) map.get("_id"),
                                        (Map<String, Map<String, Object>>) map.get("dealerInfo"), (String) map.get("dealerName"),
                                        (String) map.get("endPoint"), (ArrayList<Map<String, String>>) map.get("rideImages"),
                                        (Map<String, Object>) map.get("rideInfo"), (String) map.get("rideName"),
                                        (ArrayList<Map<String, Object>>) map.get("ridersJoined"), (String) map.get("startPoint"),
                                        (ArrayList<Long>) map.get("startPointCoordinates"));
                                mDealerUpcomingDealerUpcomingRidesResponse.add(dealerUpcomingRidesResponse);
                            }
                        }
                        RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(null);
                        RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(mDealerUpcomingDealerUpcomingRidesResponse);
                        Log.d("Firestore", "DealerUpcomingRides success size 2 is :" + mDealerUpcomingDealerUpcomingRidesResponse.size());
                        mRidesHomeView.onDealerRideResponse();
                    } catch (RuntimeException exception) {
                        mRidesHomeView.onDealerRideFailure(REApplication.getAppContext().getResources().getString(R.string.text_dealer_rides_error));
                    }
                } else if (e != null) {
                    mRidesHomeView.onDealerRideFailure(e.getMessage());
                    generateLogs(null, REFirestoreConstants.DEALER_UPCOMING_RIDES, e.getMessage());

                    Log.e("Firestore", "Failure Received onDealerUpcomingRideResponse");
                } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {

                    if (lastVisibleDealerRides == null) {
                        mRidesHomeView.onDealerRideFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_dealerrides));
                    }
                }
            });
        } catch (Exception e) {
            mRidesHomeView.onDealerRideFailure(REApplication.getAppContext().getResources().getString(R.string.text_dealer_rides_error));
        }
    }

    public void getDealerUpcomingRidesGeoQuery(RidesHomeView mRidesHomeView, double lat, double lon) {


//        // Find cities within 50km of London
        //final GeoLocation center = new GeoLocation(13.0827, 80.2707);
        //Get Geoquery radius from remote config
        String radius = REUtils.getRidesKeys().getGeoquery_radius();
        final GeoLocation center = new GeoLocation(lat, lon);
        //final double radiusInM = 20 * 1000;
        int value = Integer.parseInt(radius);
        final double radiusInM = value * 1000f;
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = FirebaseFirestore.getInstance().collection(REFirestoreConstants.DEALER_UPCOMING_RIDES)
                    .orderBy("startPointCoordinatesHash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {


                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            if (snap.size() > 0) {
                                for (DocumentSnapshot doc : snap.getDocuments()) {
                                    try {
                                        List<DealerUpcomingRidesResponse> mDealerUpcomingDealerUpcomingRidesResponse = new ArrayList<>();
                                        Map<String, Object> map = doc.getData();
                                        if (map != null) {
                                            DealerUpcomingRidesResponse dealerUpcomingRidesResponse = new DealerUpcomingRidesResponse((String) map.get("_id"),
                                                    (Map<String, Map<String, Object>>) map.get("dealerInfo"), (String) map.get("dealerName"),
                                                    (String) map.get("endPoint"), (ArrayList<Map<String, String>>) map.get("rideImages"),
                                                    (Map<String, Object>) map.get("rideInfo"), (String) map.get("rideName"),
                                                    (ArrayList<Map<String, Object>>) map.get("ridersJoined"), (String) map.get("startPoint"),
                                                    (ArrayList<Long>) map.get("startPointCoordinates"));
                                            mDealerUpcomingDealerUpcomingRidesResponse.add(dealerUpcomingRidesResponse);
                                        }
                                        com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(null);
                                        com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(mDealerUpcomingDealerUpcomingRidesResponse);
                                        Log.d("Firestore", "DealerUpcomingRides success size is :" + mDealerUpcomingDealerUpcomingRidesResponse.size());
                                        mRidesHomeView.onDealerRideResponse();
                                    } catch (RuntimeException exception) {
                                        mRidesHomeView.onDealerRideFailure(REApplication.getAppContext().getResources().getString(R.string.text_dealer_rides_error));
                                    }
                                }
                            } else {
                                mRidesHomeView.onDealerRideFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_dealerrides));
                            }
                        }

                        // matchingDocs contains the results
                        // ...
                    }
                });

    }

    /**
     * Fetches the Marquee rides from Firestore
     */
    public void getMarqueeRides(RidesHomeView mRidesHomeView) {
        Query query = FirebaseFirestore.getInstance().collection(REFirestoreConstants.MARQUEE_RIDES_FIRESTORE_KEY).orderBy(REFirestoreConstants.MARQUEE_RIDES_START_DATE, Query.Direction.DESCENDING);
        mMarqueeEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                try {
                    List<MarqueeRidesResponse> mMarqueeRidesResponse = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        mMarqueeRidesResponse.add(snapshot.toObject(MarqueeRidesResponse.class));
                    }
                    RERidesModelStore.getInstance().setMarqueeRidesResponse(null);
                    RERidesModelStore.getInstance().setMarqueeRidesResponse(mMarqueeRidesResponse);
                    Log.d("Firestore", "MarqueeRides success size is :" + mMarqueeRidesResponse.size());
                    mRidesHomeView.onMarqueeRideResponse();
                } catch (RuntimeException exception) {
                    mRidesHomeView.onMarqueeRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_marquee_rides_error));
                }
            } else if (e != null) {
                mRidesHomeView.onMarqueeRideResponseFailure(e.getMessage());
                generateLogs(null, REFirestoreConstants.MARQUEE_RIDES_FIRESTORE_KEY, e.getMessage());

                Log.e("Firestore", "Failure Received onMarqueeRideResponse");
            } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
                mRidesHomeView.onMarqueeRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_marqueerides));
            }
        });
    }

    /**
     * Fetches the Popular rides from Firestore
     */
    public void getPopularRides(RidesHomeView mRidesHomeView) {
        Query query = FirebaseFirestore.getInstance().collection(REFirestoreConstants.POPULAR_RIDES_FIRESTORE_KEY);
        mPopularEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                try {
                    List<PopularRidesResponse> mPopularRidesResponse = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        mPopularRidesResponse.add(snapshot.toObject(PopularRidesResponse.class));
                    }
                    com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore.getInstance().setPopularRidesResponse(null);
                    com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore.getInstance().setPopularRidesResponse(mPopularRidesResponse);
                    Log.d("Firestore", "PopularRides success size is :" + mPopularRidesResponse.size());
                    mRidesHomeView.onPopularRideResponse();
                } catch (RuntimeException exception) {
                    mRidesHomeView.onPopularRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_popular_rides_error));
                }
            } else if (e != null) {
                mRidesHomeView.onPopularRideResponseFailure(e.getMessage());
                generateLogs(null, REFirestoreConstants.POPULAR_RIDES_FIRESTORE_KEY, e.getMessage());
                Log.e("Firestore", "Failure Received onPopularRideResponse");
            } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
                mRidesHomeView.onPopularRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_popularrides));
            }
        });
    }

    /**
     * Iterates through the vehicleInfo Array and forms the object with Vehicle & ServiceHistory details
     *
     * @param map : Map<String, Object>
     * @return
     */
    private List<VehicleDetailsResponse> getVehicleListObject(Map<String, Object> map) {
        mVehicleDetailsList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals(REFirestoreConstants.VEHICLE_DETAILS_ARRAY)) {
                ArrayList<HashMap> arrayList = (ArrayList<HashMap>) entry.getValue();
                for (int i = 0; i < arrayList.size(); i++) {
                    VehicleDetailsResponse vehicleDetailsResponse = new VehicleDetailsResponse(arrayList.get(i)
                            .get(REFirestoreConstants.ACTIVECUSTOMERNAME), arrayList.get(i).
                            get(REFirestoreConstants.CHAISSISNO), arrayList.get(i).
                            get(REFirestoreConstants.DATEOFMFG), arrayList.get(i).
                            get(REFirestoreConstants.ENGINENO), arrayList.get(i).
                            get(REFirestoreConstants.VEHICLEDETAIL_ID), arrayList.get(i).
                            get(REFirestoreConstants.MOBILENO), arrayList.get(i).
                            get(REFirestoreConstants.MODELCODE), arrayList.get(i).
                            get(REFirestoreConstants.MODELNAME), arrayList.get(i).
                            get(REFirestoreConstants.REGISTRATION_NO), arrayList.get(i).
                            get(REFirestoreConstants.PURCHASEDATE), (ArrayList<HashMap<String, String>>) arrayList.get(i).
                            get(REFirestoreConstants.SERVICEHISTORY_INFO));
                    mVehicleDetailsList.add(vehicleDetailsResponse);
                }
            }
        }
        return mVehicleDetailsList;
    }

    public void getUserUpcomingRides(RidesHomeView ridesHomeView) {
        if (currentUserRidesQuery == null) {
            currentUserRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.USER_UPCOMING_RIDES)
                    .orderBy("createdOn", Query.Direction.DESCENDING)
                    .limit(25);
        }
        Task<QuerySnapshot> mUserUpcomingEventListenernew = currentUserRidesQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                    try {
                        lastVisibleUserRides = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                        if (lastVisibleUserRides != null) {
                            currentUserRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.USER_UPCOMING_RIDES)
                                    .orderBy("createdOn", Query.Direction.DESCENDING)
                                    .startAfter(lastVisibleUserRides)
                                    .limit(25);
                        }
                        List<UserUpcomingRidesResponse> mUserUpcomingRidesResponseList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            mUserUpcomingRidesResponseList.add(snapshot.toObject(UserUpcomingRidesResponse.class));
                        }
                        RERidesModelStore.getInstance().setUserUpcomingRidesResponse(null);
                        RERidesModelStore.getInstance().setUserUpcomingRidesResponse(mUserUpcomingRidesResponseList);
                        Log.d("Firestore", "UserUpcomingRides success size 2 is :" + mUserUpcomingRidesResponseList.size());
                        ridesHomeView.onUserCreatedRideResponse();
                    } catch (Exception e) {
                        ridesHomeView.onUserCreatedRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_user_rides_error));
                    }
                } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
                    if (lastVisibleUserRides == null) {
                        ridesHomeView.onUserCreatedRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_userrides));
                    }
                }
            }
        });
    }

    /**
     * Fetches the User upcoming rides from FireStore
     */
    public void getUserUpcomingRides1(RidesHomeView ridesHomeView) {
        try {

            if (currentUserRidesQuery == null) {
                currentUserRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.USER_UPCOMING_RIDES)
                        .orderBy("createdOn", Query.Direction.DESCENDING)
                        .limit(25);
            }
            mUserUpcomingEventListener = currentUserRidesQuery.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e == null && queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                    try {
                        lastVisibleUserRides = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                        if (lastVisibleUserRides != null) {
                            currentUserRidesQuery = FirebaseFirestore.getInstance().collection(REFirestoreConstants.USER_UPCOMING_RIDES)
                                    .orderBy("createdOn", Query.Direction.DESCENDING)
                                    .startAfter(lastVisibleUserRides)
                                    .limit(25);
                        }
                        List<UserUpcomingRidesResponse> mUserUpcomingRidesResponseList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            mUserUpcomingRidesResponseList.add(snapshot.toObject(UserUpcomingRidesResponse.class));
                        }
                        RERidesModelStore.getInstance().setUserUpcomingRidesResponse(null);
                        RERidesModelStore.getInstance().setUserUpcomingRidesResponse(mUserUpcomingRidesResponseList);
                        Log.d("Firestore", "UserUpcomingRides success size 2 is :" + mUserUpcomingRidesResponseList.size());
                        ridesHomeView.onUserCreatedRideResponse();
                    } catch (RuntimeException exception) {
                        ridesHomeView.onUserCreatedRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_user_rides_error));
                    }
                } else if (e != null) {
                    ridesHomeView.onUserCreatedRideResponseFailure(e.getMessage());
                    generateLogs(null, REFirestoreConstants.USER_UPCOMING_RIDES, e.getMessage());
                    Log.e("Firestore", "Failure Received onUserUpcomingRidesResponse");
                } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
                    if (lastVisibleUserRides == null) {
                        ridesHomeView.onUserCreatedRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_userrides));
                    }
                }
            });
//            Query query = FirebaseFirestore.getInstance().collection(REFirestoreConstants.USER_UPCOMING_RIDES).orderBy("createdOn").limit(25);
//            mUserUpcomingEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
//                if (e == null && queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
//                    try {
//                        List<UserUpcomingRidesResponse> mUserUpcomingRidesResponseList = new ArrayList<>();
//                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                            mUserUpcomingRidesResponseList.add(snapshot.toObject(UserUpcomingRidesResponse.class));
//                        }
//                        RERidesModelStore.getInstance().setUserUpcomingRidesResponse(null);
//                        RERidesModelStore.getInstance().setUserUpcomingRidesResponse(mUserUpcomingRidesResponseList);
//                        Log.d("Firestore", "UserUpcomingRides success size is :" + mUserUpcomingRidesResponseList.size());
//                        ridesHomeView.onUserCreatedRideResponse();
//                    } catch (RuntimeException exception) {
//                        ridesHomeView.onUserCreatedRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_user_rides_error));
//                    }
//                } else if (e != null) {
//                    ridesHomeView.onUserCreatedRideResponseFailure(e.getMessage());
//                    generateLogs(null, REFirestoreConstants.USER_UPCOMING_RIDES, e.getMessage());
//                    Log.e("Firestore", "Failure Received onUserUpcomingRidesResponse");
//                } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
//                    ridesHomeView.onUserCreatedRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_userrides));
//                }
//            });
        } catch (Exception e) {
          //  e.printStackTrace();
        }
    }

    public void getUserUpcomingRidesGeoQuery(RidesHomeView ridesHomeView, double lat, double lon) {


//        // Find cities within 50km of London
        //final GeoLocation center = new GeoLocation(10.0904849, 76.3371358);
        //Get Geoquery radius from remote config
        String radius = REUtils.getRidesKeys().getGeoquery_radius();
        final GeoLocation center = new GeoLocation(lat, lon);
        //final double radiusInM = 20 * 1000;
        int value = Integer.parseInt(radius);
        final double radiusInM = value * 1000f;
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = FirebaseFirestore.getInstance().collection(REFirestoreConstants.USER_UPCOMING_RIDES)
                    .orderBy("startPointCoordinatesHash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {


                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            if (snap.size() > 0) {
                                try {
                                    List<UserUpcomingRidesResponse> mUserUpcomingRidesResponseList = new ArrayList<>();
                                    for (DocumentSnapshot snapshot : snap.getDocuments()) {
                                        mUserUpcomingRidesResponseList.add(snapshot.toObject(UserUpcomingRidesResponse.class));
                                    }
                                    RERidesModelStore.getInstance().setUserUpcomingRidesResponse(null);
                                    RERidesModelStore.getInstance().setUserUpcomingRidesResponse(mUserUpcomingRidesResponseList);
                                    Log.d("Firestore", "UserUpcomingRides success size is :" + mUserUpcomingRidesResponseList.size());
                                    ridesHomeView.onUserCreatedRideResponse();
                                } catch (RuntimeException exception) {
                                    ridesHomeView.onUserCreatedRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_user_rides_error));
                                }
                            } else {
                                ridesHomeView.onUserCreatedRideResponseFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_dealerrides));
                            }
                        }

                        // matchingDocs contains the results
                        // ...
                    }
                });

    }

    public void removeDealerRidesListener() {
        try {
            currentDealerRidesQuery = null;
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public void removeUserRidesListener() {
        try {
            currentUserRidesQuery = null;
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * Fetches the News details from FireStore
     */
    public void getNewsDetails(OurWorldView ourWorldView) {
        Query query = FirebaseFirestore.getInstance().collection(REFirestoreConstants.OUR_WORLD_NEWS_FIRE_STORE_DATA).orderBy(REFirestoreConstants.OUR_WORLD_NEWS_CREATED_DATE, Query.Direction.DESCENDING);
        mNewsEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                try {
                    List<NewsResponse> mNewsResponseList = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        mNewsResponseList.add(snapshot.toObject(NewsResponse.class));
                    }
                    Log.d("Firestore", "NewsDetails success size is :" + mNewsResponseList.size());
                    ourWorldView.onOurWorldNewsSuccess(mNewsResponseList);
                } catch (RuntimeException exception) {
                    ourWorldView.onOurWorldNewsFailure(REApplication.getAppContext().getResources().getString(R.string.text_news_error));
                }
            } else if (e != null) {
                ourWorldView.onOurWorldNewsFailure(e.getMessage());
                generateLogs(null, REFirestoreConstants.OUR_WORLD_NEWS_FIRE_STORE_DATA, e.getMessage());
                Log.e("Firestore", "Failure Received onGetNewsDetailsResponse");
            } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
                ourWorldView.onOurWorldNewsFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_news));
            }
        });
    }

    /**
     * Fetches the pendingRideInfo from Firestore
     */
    public void getPendingRideInfo() {
        Query query = FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(REFirestoreConstants.USER_RIDES).
                collection(REFirestoreConstants.USER_PROFILE_PENDING_RIDES).orderBy(REFirestoreConstants.
                USER_PROFILE_START_DATE, Query.Direction.ASCENDING).limit(150);
        mPendingRideEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null) {
                try {
                    List<ProfileRidesResponse> profileRidesResponse = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        profileRidesResponse.add(snapshot.toObject(ProfileRidesResponse.class));
                    }
                    REUserModelStore.getInstance().setPendingRideResponse(profileRidesResponse);
                    // Sending broadcast to update the view
                    Intent pendingRideUpdate = new Intent(REConstants.FIRESTORE_PENDING_RIDE);
                    LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(pendingRideUpdate);
                } catch (RuntimeException exception) {
                    RELog.e(exception);
                }
            } else if (e != null) {
                generateLogs(null, REUtils.getUserId() + "/" + REFirestoreConstants.USER_PROFILE_PENDING_RIDES, e.getMessage());
            }
        });
    }

    /**
     * Fetches the ongoingRideInfo from Firestore
     */
    public void getOngoingRideInfo() {
        Query query = FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(REFirestoreConstants.USER_RIDES).
                collection(REFirestoreConstants.USER_PROFILE_ONGOING_RIDES);
        mOngoingEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null) {
                try {
                    List<ProfileRidesResponse> profileRidesResponse = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        profileRidesResponse.add(snapshot.toObject(ProfileRidesResponse.class));
                    }
                    REUserModelStore.getInstance().setOngoingRideResponse(profileRidesResponse);
                    // Sending broadcast to update the text
                    Intent ongoingRideUpdate = new Intent(REConstants.FIRESTORE_ONGOING_RIDE);
                    LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(ongoingRideUpdate);
                } catch (RuntimeException exception) {

                    RELog.e(exception);
                }
            } else if (e != null) {
                generateLogs(null, REUtils.getUserId() + "/" + REFirestoreConstants.USER_PROFILE_ONGOING_RIDES, e.getMessage());
            }
        });
    }

    /**
     * Fetches the pastRideInfo from Firestore
     */
    public void getPastRideInfo() {
        Query query = FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(REFirestoreConstants.USER_RIDES).
                collection(REFirestoreConstants.USER_PROFILE_PAST_RIDES).orderBy(REFirestoreConstants.USER_PROFILE_START_DATE, Query.Direction.DESCENDING).
                limit(150);
        mPastRideEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null) {
                try {
                    List<ProfileRidesResponse> profileRidesResponse = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        profileRidesResponse.add(snapshot.toObject(ProfileRidesResponse.class));
                    }
                    REUserModelStore.getInstance().setPastRideResponse(profileRidesResponse);
                    // Sending broadcast to update the view
                    Intent pastRideUpdate = new Intent(REConstants.FIRESTORE_PAST_RIDE);
                    LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(pastRideUpdate);
                } catch (RuntimeException exception) {
                    RELog.e(exception);
                }
            } else if (e != null) {
                generateLogs(null, REUtils.getUserId() + "/" + REFirestoreConstants.USER_PROFILE_PAST_RIDES, e.getMessage());
            }
        });
    }

    /**
     * Fetches the upcomingRideInfo from Firestore
     */
    public void getUpcomingRideInfo() {
        Query query = FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(REFirestoreConstants.USER_RIDES).
                collection(REFirestoreConstants.USER_PROFILE_UPCOMING_RIDES).orderBy(REFirestoreConstants.USER_PROFILE_START_DATE, Query.Direction.ASCENDING).
                limit(150);
        mUpcomingEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null) {
                try {
                    List<ProfileRidesResponse> profileRidesResponse = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        profileRidesResponse.add(snapshot.toObject(ProfileRidesResponse.class));
                    }
                    REUserModelStore.getInstance().setUpcomingRideResponse(profileRidesResponse);
                    // Sending broadcast to update the view
                    Intent upcomingRideUpdate = new Intent(REConstants.FIRESTORE_UPCOMING_RIDE);
                    LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(upcomingRideUpdate);
                } catch (RuntimeException exception) {
                    RELog.e(exception);
                }
            } else if (e != null) {
                generateLogs(null, REUtils.getUserId() + "/" + REFirestoreConstants.USER_PROFILE_UPCOMING_RIDES, e.getMessage());
            }
        });
    }

    /**
     * Fetches the rejectedRideInfo from Firestore
     */
    public void getRejectedRideInfo() {
        Query query = FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(REFirestoreConstants.USER_RIDES).
                collection(REFirestoreConstants.USER_PROFILE_REJECTED_RIDES).orderBy(REFirestoreConstants.USER_PROFILE_START_DATE, Query.Direction.ASCENDING).
                limit(150);
        mRejectedEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null) {
                try {
                    List<ProfileRidesResponse> profileRidesResponse = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        profileRidesResponse.add(snapshot.toObject(ProfileRidesResponse.class));
                    }
                    REUserModelStore.getInstance().setRejectedRideResponse(profileRidesResponse);
                    // Sending broadcast to update the view
                    Intent rejectedRideUpdate = new Intent(REConstants.FIRESTORE_REJECTED_RIDE);
                    LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(rejectedRideUpdate);
                } catch (RuntimeException exception) {
                    RELog.e(exception);
                }
            } else if (e != null) {
                generateLogs(null, REUtils.getUserId() + "/" + REFirestoreConstants.USER_PROFILE_REJECTED_RIDES, e.getMessage());
            }
        });
    }


    /**
     * Fetches the Events details from FireStore
     */
    public void getEventsDetails(OurWorldView ourWorldView) {
        Query query = FirebaseFirestore.getInstance().collection(REFirestoreConstants.OUR_WORLD_EVENTS_FIRE_STORE_DATA);
        mEventsEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                try {
                    List<EventsResponse> mEventsResponseList = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        mEventsResponseList.add(snapshot.toObject(EventsResponse.class));
                    }
                    RERidesModelStore.getInstance().setOurWorldEventsResponse(null);
                    RERidesModelStore.getInstance().setOurWorldEventsResponse(mEventsResponseList);
                    Log.d("Firestore", "EventDetails success size is :" + mEventsResponseList.size());
                    ourWorldView.onOurWorldEventSuccess(mEventsResponseList);
                } catch (RuntimeException exception) {
                    ourWorldView.onOurWorldEventFailure(REApplication.getAppContext().getResources().getString(R.string.text_events_error));
                }
            } else if (e != null) {
                ourWorldView.onOurWorldEventFailure(e.getMessage());
                generateLogs(null, REFirestoreConstants.OUR_WORLD_EVENTS_FIRE_STORE_DATA, e.getMessage());

                Log.e("Firestore", "Failure Received onGetEventDetailsResponse");
            } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
                ourWorldView.onOurWorldEventFailure(REApplication.getAppContext().getResources().getString(R.string.text_no_events));
            }
        });
    }

    public void getAllRidesInfoFromFirestore() {
        if (REUtils.getUserId() != null&&REApplication.getInstance().mFirebaseAuth!=null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser()!=null) {
//            FirestoreManager.getInstance().getUserInfo();
            try {
                //Gets rides data from firestore
                FirestoreManager.getInstance().getOngoingRideInfo();
                FirestoreManager.getInstance().getPendingRideInfo();
                FirestoreManager.getInstance().getPastRideInfo();
                FirestoreManager.getInstance().getUpcomingRideInfo();
                FirestoreManager.getInstance().getRejectedRideInfo();
            } catch (IllegalArgumentException e) {
                RELog.e(e);
            }
        }
    }

    public void getServiceHistory() {
        if (REUtils.getUserId() != null) {
            try {
                Query query = FirebaseFirestore.getInstance().collection(REUtils.getUserId()).document(
                        REFirestoreConstants.VEHICLE_SERVICE_HISTORY)
                        .collection(REFirestoreConstants.SERVICE_HISTORY);
                mServiceHistoryListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
                    List<VehicleDataModel> mVehicleDetailsList = new ArrayList<>();
                    mVehicleDetailsList = REServiceSharedPreference.getVehicleData(REApplication.getAppContext());
                    if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
                        if (e == null && queryDocumentSnapshots != null) {
                            try {
                                List<ServiceHistoryResponse> serviceHistoryResponses = new ArrayList<>();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    serviceHistoryResponses.add(snapshot.toObject(ServiceHistoryResponse.class));
                                }
                                for (int i = 0; i < serviceHistoryResponses.size(); i++) {
                                    String chassisNo = serviceHistoryResponses.get(i).getChassisNo();
                                    if (chassisNo != null) {
                                        for (int j = 0; j < mVehicleDetailsList.size(); j++) {
                                            if (chassisNo.equals(mVehicleDetailsList.get(j).getChaissisNo())) {
                                                mVehicleDetailsList.get(j).insertServiceHistoryResponse(serviceHistoryResponses.get(i));
                                            }
                                        }
                                    }
                                }
                                REServiceSharedPreference.saveVehicleData(REApplication.getAppContext(), mVehicleDetailsList);
                            } catch (RuntimeException exception) {
                                RELog.e(exception);
                            }
                        } else if (e != null) {
                            generateLogs(null,REFirestoreConstants.VEHICLE_SERVICE_HISTORY , e.getMessage());

                        }
                    }
                });
            } catch (IllegalArgumentException e) {
                //Exception handled for intermittent crash
                RELog.e(e);
            }

        }
    }

    public void getRealTimeVehicleDetails(OnRealtimeVehicledetailListner onRealtimeVehicledetailListner) {
        final DocumentReference vehicelDocRef = REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document(REFirestoreConstants.VEHICLE_DOCUMENT);
        mrealTimeVehicelDetailListener = vehicelDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
				REUtils.CHECK_VEHICLE_SYNCING_FAILED =false;
				REUtils.CHECK_VEHICLE_PENDING=false;
                if (e != null) {
                    // generateLogs(null, REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId() + "/" + REFirestoreConstants.VEHICLE_DOCUMENT, e.getMessage());

                    RELog.d(TAG, e.getMessage()+"Listen failed.");
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(snapshot.getData());
					try {
						VehicleDetailResponseFirestore vehicleDetailResponseFirestore = gson.fromJson(jsonElement, VehicleDetailResponseFirestore.class);
						String status = vehicleDetailResponseFirestore.getFirebaseStatus();
						if (status != null) {
							switch (status) {
								case "FAILED":
									REUtils.CHECK_VEHICLE_SYNCING_FAILED = true;
									break;
								case "PENDING":
									REUtils.CHECK_VEHICLE_PENDING = true;
									break;
								default:
									break;
							}
						}

						List<VehicleDataModel> list = mapVehicleFirestoreResponseToVehicleViewModel(vehicleDetailResponseFirestore);
						REServiceSharedPreference.saveVehicleData(REApplication.getAppContext(), list);
						onRealtimeVehicledetailListner.onVehicleDetailSuccessFireStore(list);
						Log.d(TAG, "VehicleDetailResponseFirestore" + jsonElement.toString());
					}
					catch (Exception exception){
						REServiceSharedPreference.saveVehicleData(REApplication.getAppContext(), new ArrayList<VehicleDataModel>());
						onRealtimeVehicledetailListner.onVehicleDetailSuccessFireStore(new ArrayList<VehicleDataModel>());
					}
                } else {
                    Log.d(TAG, "Current data: null");
                    REServiceSharedPreference.saveVehicleData(REApplication.getAppContext(), new ArrayList<VehicleDataModel>());
                    onRealtimeVehicledetailListner.onVehicleDetailSuccessFireStore(new ArrayList<VehicleDataModel>());
                }
            }
        });
    }

    public void removeRealTimeVehicelDetailListener() {
        try {
            mrealTimeVehicelDetailListener.remove();
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public List<VehicleDataModel> mapVehicleFirestoreResponseToVehicleViewModel(VehicleDetailResponseFirestore vehicleDetailResponseFirestore) {
        List<VehicleDataModel> list = new ArrayList<>();
        boolean flag = false;
        if (vehicleDetailResponseFirestore != null&&vehicleDetailResponseFirestore.getUserVehicles()!=null) {
            for (UserVehicle userVehicle : vehicleDetailResponseFirestore.getUserVehicles()) {
                if (userVehicle.getVehicleStatus().equalsIgnoreCase("VERIFIED")) {
                    list.add(new HomeActivityPresenter.VehicleDataMapper().mapFirestoreData(userVehicle));
                    flag = true;
                }
            }
            if (flag) {
                REUtils.CHECK_VEHICLE_PENDING = false;
            } else if (vehicleDetailResponseFirestore.getUserVehicles().size() > 0 && !flag) {
                REUtils.CHECK_VEHICLE_PENDING = true;
            }
        }
        return list;
    }

    /**
     * Removes the firestore event listeners
     */
    public void removeFirestoreEvents() {
        try {
            if (REApplication.getInstance().mFirebaseAuth != null)
                REApplication.getInstance().mFirebaseAuth.signOut();
            mPendingRideEventListener.remove();
            mUpcomingEventListener.remove();
            mPastRideEventListener.remove();
            mOngoingEventListener.remove();
            mRejectedEventListener.remove();
            mDealerUpcomingEventListener.remove();
            mPopularEventListener.remove();
            mMarqueeEventListener.remove();
            mUserUpcomingEventListener.remove();
            mNewsEventListener.remove();
            mEventsEventListener.remove();
            mServiceHistoryListener.remove();
            mUserInfoEventListener.remove();
			mrealTimeVehicelDetailListener.remove();
			mRealtimeProfileListener.remove();
            mUserConsentListener.remove();
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }



//    public void readPersonalDetailUpdateFirestore(OnFirestoreUserSettingCallback onFirestoreUserSettingCallback) {
//        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser() != null) {
//            REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document("userInfo")
//                    .get().addOnSuccessListener(documentSnapshot -> {
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//
//                    try {
//                        Map<String, Object> map = (Map<String, Object>) documentSnapshot.get("accountStatus");
//                        if (map != null && map.size() > 0 && onFirestoreUserSettingCallback != null) {
//                             REUtils.UPDATE_USER_PERSONAL_DETAIL=(Boolean) map.get("isUserProfileUpdated");
//
//                        }
//                    } catch (Exception exception) {
//                        onFirestoreUserSettingCallback.onUpdatePersonalDetailSuccess(true);
//                        RELog.e(exception);
//                        return;
//                    }
//                    if (onFirestoreUserSettingCallback != null)
//                        onFirestoreUserSettingCallback.onUpdatePersonalDetailSuccess(REUtils.UPDATE_USER_PERSONAL_DETAIL);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
//                    onFirestoreUserSettingCallback.onFirestoreUserSettingFailure("Error getting data!!!");
//                }
//            });
//        }
//    }





    public static void createConnectedInfoToFirestore(String chassisNo, String deviceIMei, boolean isConsentTaken
            , OnAuthorizationCallback authorizationCallback) {
        Map<String, Map<String, Object>> connectedInfo = new HashMap<>();
        Map<String, Object> chassisDetail = new HashMap<>();
        chassisDetail.put(REConstants.DEVICE_IMEI, deviceIMei);
        chassisDetail.put(REConstants.IS_CONSENT_TAKEN, isConsentTaken);
        connectedInfo.put(chassisNo, chassisDetail);
        REApplication.mFireStoreInstance.collection(
                REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())
                .document(REFirestoreConstants.CONNECTED_DOCUMENT)
                .set(connectedInfo, SetOptions.merge())
                .addOnSuccessListener(
                        success -> readConnectedInfoFireStore(chassisNo, authorizationCallback)
                )
                .addOnFailureListener(failure -> {
                    if (authorizationCallback != null) {
                        authorizationCallback.onAuthorizeFailure(failure.getMessage());
                    }
                });
    }

    public void updateUserSettingFirebaseField(Map<String, Object> value) {
        REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document(REFirestoreConstants.USER_SETTINGS)
                .set(value,SetOptions.merge())
                .addOnSuccessListener(success -> {
                })
                .addOnFailureListener(failure -> {
                    Log.d("mFireStoreInstance", failure.toString());
                });
    }

    public void updateProvisionTableDataToFirestore(String data, OnFirestoreCallback onFirestoreUserSettingCallback) {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd-MM-yyyy");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date=dateFormatGmt.format(new Date())+"";
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser() != null) {
            REApplication.mFireStoreInstance.collection(CONNECTED_PROVISIONING).document(date)
                    .get().addOnSuccessListener(documentSnapshot -> {
                try {

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        checkForUserDocumentExist(data,date,REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId(),onFirestoreUserSettingCallback);
                    } else {
                        if (onFirestoreUserSettingCallback != null)
                            createDocumentForProvisioning(data,date,REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId() ,onFirestoreUserSettingCallback);
                    }
                }
                catch (Exception e){
                    generateLogs(null, REFirestoreConstants.USER_SETTINGS,e.getMessage()!=null?e.getMessage():"");
                    //  REUtils.logPrint(e.getMessage()!=null?e.getMessage():"",REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
                    if (onFirestoreUserSettingCallback != null)
                        onFirestoreUserSettingCallback.onFirestoreFailure("");
                }
            }).addOnFailureListener(e ->
                    RELog.e(e));
        }
    }

    private void createDocumentForProvisioning(String data, String date, String userId, OnFirestoreCallback onFirestoreUserSettingCallback) {

        Map<String, String> onBoardingFlags = new HashMap<>();
        onBoardingFlags.put("data",data);
        REApplication.mFireStoreInstance.collection(CONNECTED_PROVISIONING).document(date).collection(userId).document(userId)
                .set(onBoardingFlags)
                .addOnSuccessListener(success->{
                    REApplication.mFireStoreInstance.collection(CONNECTED_PROVISIONING).document(date).update("test","test");
                    Map<String ,Object> dummyMap= new HashMap<>();
                    DocumentReference df= REApplication.mFireStoreInstance.collection("connected_provisioning_data").document(date);
                    df.set(dummyMap);  // add empty field, wont shown in console
                    df.collection("connected_provisioning_data");
                    onFirestoreUserSettingCallback.onFirestoreSuccess();
                })
                .addOnFailureListener(failure -> onFirestoreUserSettingCallback.onFirestoreFailure(failure.getMessage()));

    }

    private void checkForUserDocumentExist(String data, String date, String user, OnFirestoreCallback onFirestoreUserSettingCallback) {
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser() != null) {
            REApplication.mFireStoreInstance.collection(CONNECTED_PROVISIONING).document(date).collection(user).document(user)
                    .get().addOnSuccessListener(documentSnapshot -> {
                try {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        updateData(data,date,user,onFirestoreUserSettingCallback);
                    }


                     else {
                        createDocumentForProvisioning(data,date,user,onFirestoreUserSettingCallback);
                    }
                }
                catch (Exception e){
                    generateLogs(null, REFirestoreConstants.USER_SETTINGS,e.getMessage()!=null?e.getMessage():"");
                    //  REUtils.logPrint(e.getMessage()!=null?e.getMessage():"",REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
                    if (onFirestoreUserSettingCallback != null)
                        onFirestoreUserSettingCallback.onFirestoreFailure("");
                }
            }).addOnFailureListener(e ->
               RELog.e(e));
        }

    }

    private void updateData(String data, String date, String user, OnFirestoreCallback onFirestoreUserSettingCallback) {
        Map<String, Object> onBoardingFlags = new HashMap<>();
        onBoardingFlags.put("data",data);
        REApplication.mFireStoreInstance.collection(CONNECTED_PROVISIONING).document(date).collection(user).document(user).update(onBoardingFlags) .addOnSuccessListener(success->onFirestoreUserSettingCallback.onFirestoreSuccess())
                .addOnFailureListener(failure -> onFirestoreUserSettingCallback.onFirestoreFailure(failure.getMessage()));

    }


    public void getOTAPInfo(OnOTAPCallback mOnOTAPCallback) {
        Query query = FirebaseFirestore.getInstance().collection(REFirestoreConstants.RE_FOTA);
        mOTAPEventListener = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e == null && queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                try {
                    Map<String, Object> map = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1).getData();
                    if (map != null) {
                        mOnOTAPCallback.onOTAPFirestoreSuccess(map);
                    } else {
                        mOnOTAPCallback.onOTAPFirestoreFailure("map == null");
                    }
                } catch (RuntimeException exception) {
                    mOnOTAPCallback.onOTAPFirestoreFailure(exception.getMessage());
                }
            } else if (e != null) {
                mOnOTAPCallback.onOTAPFirestoreFailure("FirebaseFirestoreException:" + e.getMessage());
            } else if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0) {
                mOnOTAPCallback.onOTAPFirestoreFailure("queryDocumentSnapshots == null || queryDocumentSnapshots.size() == 0");
            }
        });
    }

//    public void getCountryData(OnCountryDataListener callback) {
//        DatabaseReference myRef = FirebaseDatabase
//                .getInstance(REConstants.FirebaseCountryUrl).getReference();
//
//        myRef.child("country").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                List<CountryModel> countryModels = new ArrayList<>();
//
//                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
//                    countryModels.add(new CountryModel(dataSnapshotChild.child("description").getValue().toString()
//                            , dataSnapshotChild.child("diallingcode").getValue().toString()
//                            , dataSnapshotChild.child("code").getValue().toString()
//                            , (Boolean) dataSnapshotChild.child("showInCountryList").getValue()));
//                }
//                if (callback != null)
//                    callback.onSuccess(countryModels);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                generateLogs(null, REConstants.FirebaseCountryUrl, databaseError.getMessage());
//                callback.onFailure(databaseError.getMessage());
//            }
//        });
//    }

//    public void getHtmlData(Context context, String country) {
//        if (REConstants.FirebaseStaticUrl != null) {
//            DatabaseReference myRef = FirebaseDatabase
//                    .getInstance(REConstants.FirebaseStaticUrl).getReference();
//
//            myRef.child("static").child(country).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String ew = dataSnapshot.child("extended-warranty").child("url").getValue(String.class);
//                        String faq = dataSnapshot.child("faqs").child("url").getValue(String.class);
//                        String rsa = dataSnapshot.child("rsa").child("url").getValue(String.class);
//                        String tnc = dataSnapshot.child("terms-and-conditions").child("url").getValue(String.class);
//                        String rtc = dataSnapshot.child("create-ride-terms").child("url").getValue(String.class);
//                        String diyContainerName = dataSnapshot.child("diyVideos").child("containerName").getValue(String.class);
//                        String diyDirName = dataSnapshot.child("diyVideos").child("dirName").getValue(String.class);
//                        String diyFileName = dataSnapshot.child("diyVideos").child("fileName").getValue(String.class);
//                        String tncDrsa = dataSnapshot.child("terms-and-conditions-drsa").child("url").getValue(String.class);
//
//
//                        try {
//                            REPreference.getInstance().putString(context, REConstants.KEY_FAQ, faq);
//                            REPreference.getInstance().putString(context, REConstants.KEY_EW, ew);
//                            REPreference.getInstance().putString(context, REConstants.KEY_RSA, rsa);
//                            REPreference.getInstance().putString(context, REConstants.KEY_TNC, tnc);
//                            REPreference.getInstance().putString(context, REConstants.KEY_RTC, rtc);
//                            REPreference.getInstance().putString(context, REConstants.KEY_TNC_DRSA, tncDrsa);
//                            REPreference.getInstance().putString(context, REConstants.KEY_DIY_CONTAINER, diyContainerName);
//                            REPreference.getInstance().putString(context, REConstants.KEY_DIY_DIRNAME, diyDirName);
//                            REPreference.getInstance().putString(context, REConstants.KEY_DIY_FILENAME, diyFileName);
//                        } catch (PreferenceException e) {
//                            RELog.e(e);
//                        }
//
//                    } else {
//                        if (!country.equalsIgnoreCase("default"))
//                            getHtmlData(context, "default");
//                    }
//
//
//                    // callback.onSuccess(countryModels);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    generateLogs(null, REConstants.FirebaseStaticUrl, databaseError.getMessage());
//
//                }
//            });
//        }
//    }


    private void getCountryListJson(String url) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getJsonAPI()
                .getCountryJson(url)
                .enqueue(new BaseCallback<CountryListModel>() {
                    @Override
                    public void onResponse(@NotNull Call<CountryListModel> call, @NotNull Response<CountryListModel> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                REPreference.getInstance().putString(REApplication.getAppContext(), COUNTRY_URL_KEY, url);
                                REApplication.getInstance().setCountryList(response.body().getCountry());
                                Intent intent = new Intent("country_data");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            } catch (PreferenceException e) {
                                RELog.e(e);
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<CountryListModel> call, @NotNull Throwable t) {
                        super.onFailure(call, t);

                    }
                });

    }

    public void getUserConsent(OnConsentDataListener listener) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document(REFirestoreConstants.CONSENT_COLLECTION);


        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (listener != null) {
                        listener.onSuccess(document.toObject(RequestConsent.class));
                    }
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    if (listener != null) {
                        listener.onFailure("");
                    }
                    generateLogs(null, REFirestoreConstants.CONSENT_COLLECTION, "");

                    Log.d(TAG, "No such document");
                }
            } else {
                if (listener != null) {
                    listener.onFailure("");
                }
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }
    public void updateAlertTimestamp(String timestamp,String chesis,OnFirestoreCallback onFirestoreCallback) {
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser() != null) {
            REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document("connectedInfo")
                    .get().addOnSuccessListener(documentSnapshot -> {
                try {
                    if (documentSnapshot != null && documentSnapshot.exists() && documentSnapshot.contains(chesis)) {
                        Map<String, Object> deviceData = (Map<String, Object>) documentSnapshot.get(chesis);
                        deviceData.put("alert_timestamp", timestamp);
                        updateTimestamp(chesis,deviceData, onFirestoreCallback);
                    }
                }

                catch (Exception e){
                    generateLogs(null, REFirestoreConstants.USER_SETTINGS,e.getMessage()!=null?e.getMessage():"");
                    if (onFirestoreCallback != null)
                        onFirestoreCallback.onFirestoreFailure("");
                }
            }).addOnFailureListener(e ->
                 RELog.e(e));
        }
    }

    private void updateTimestamp(String chesssis,Map<String, Object> onBoardingFlags,OnFirestoreCallback onFirestoreCallback) {
            REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document("connectedInfo").update(chesssis,onBoardingFlags) .addOnSuccessListener(success->onFirestoreCallback.onFirestoreSuccess())
                    .addOnFailureListener(failure -> onFirestoreCallback.onFirestoreFailure(failure.getMessage()));

    }
    public static void readConnectedInfoFireStore(String chassisNo, OnAuthorizationCallback authorizationCallback) {
        REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())
                .document(REFirestoreConstants.CONNECTED_DOCUMENT)
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists() && documentSnapshot.contains(chassisNo)) {
                HashMap deviceData = (HashMap) documentSnapshot.get(chassisNo);
                String deviceIMei = (String) deviceData.get(REConstants.DEVICE_IMEI);
                boolean isConsentTaken = false;
                if (deviceData.size() > 1) {
                    isConsentTaken = (boolean) deviceData.get(REConstants.IS_CONSENT_TAKEN);
                }
                if (authorizationCallback != null) {
                    if (deviceIMei != null) {
                        authorizationCallback.onAuthorizeSuccess(deviceIMei, isConsentTaken);
                    } else {
                        authorizationCallback.onAuthorizeFailure("Not authorized");
                    }
                }

            } else {
                if (authorizationCallback != null) {
                    authorizationCallback.onAuthorizeFailure("Not authorized");
                }
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show()
        );
    }


    public void getConnnectedDocument(String chesis,OnFirestoreCallback authorizationCallback) {
        if(mAlertTimeListener!=null){
            mAlertTimeListener.remove();
        }
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser() != null) {
            final DocumentReference docRef = REApplication.mFireStoreInstance.collection(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()).document("connectedInfo");
            mAlertTimeListener = docRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()&&snapshot.contains(chesis)) {
                        HashMap deviceData = (HashMap) snapshot.get(chesis);
                        String deviceIMei = (String) deviceData.get(REConstants.DEVICE_IMEI);
                    String timestamp=   (String) deviceData.get(REConstants.ALERT_TIMESTAMP);
                    REApplication.getInstance().setAlertTimestamp(timestamp);
                        if (authorizationCallback != null) {
                            if (deviceIMei != null) {
                                authorizationCallback.onFirestoreSuccess();
                            } else {
                                authorizationCallback.onFirestoreFailure("Not authorized");
                            }
                        }

                    } else {
                        if (authorizationCallback != null) {
                            authorizationCallback.onFirestoreFailure("Not authorized");
                        }
                    }
            });
        }
    }


    public void getSecretKey( OnFirestoreCallback onFirestoreUserSettingCallback) {
          REApplication.mFireStoreInstance.collection(APP_SECRETS).document("connected")
                    .get().addOnSuccessListener(documentSnapshot -> {
                try {
                    if (documentSnapshot != null && documentSnapshot.exists()) {

                        Map<String, Object> map = documentSnapshot.getData();
                        if (map != null) {
                           Log.e("Secret Key",map.get("encryptionKey").toString());
                           REApplication.getInstance().setSecret(map.get("encryptionKey").toString());
                        }

                    }
                }
                catch (Exception e){
                    generateLogs(null, REFirestoreConstants.APP_SECRETS,e.getMessage()!=null?e.getMessage():"");
                    //  REUtils.logPrint(e.getMessage()!=null?e.getMessage():"",REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
                    if (onFirestoreUserSettingCallback != null)
                        onFirestoreUserSettingCallback.onFirestoreFailure("");
                }
            }).addOnFailureListener(e ->
                    RELog.e(e));
    }

    public void getRemoteConfigDataVersion() {
		long startTime=System.currentTimeMillis();
		REApplication.mFireStoreInstance.collection(REMOTE_CONFIG).document("version")
                .get().addOnSuccessListener(documentSnapshot -> {
            try {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                     String id = documentSnapshot.get("id").toString();
                        String version = getStringSharedpreference(REApplication.getAppContext(),REMOTE_VERSION);
                    String data = getStringSharedpreference(REApplication.getAppContext(),REMOTE_DATA);
                	REUtils.sendResponseLog("Remote config version Firebase", "", (double)(System.currentTimeMillis()-startTime) / 1000);
                        if (version.isEmpty() ||data.isEmpty() ||version.equalsIgnoreCase("0.0")||!version.equalsIgnoreCase(id)) {
                                getRemoteConfigDataFromFirestore(id);
                        } else {

                         //   RemoteConfigData dataset = new Gson().fromJson(data, RemoteConfigData.class);
                          //  REApplication.getInstance().setRemoteConfigData(dataset);
                        }
                }
                else{
					remoteConfigFailure("No document exist");
                }
            } catch (Exception e) {

				remoteConfigFailure(e.getMessage());
            }
        }).addOnFailureListener(e ->
        {
            FirebaseCrashlytics.getInstance().recordException(e);
			remoteConfigFailure(e.getMessage());
        });
    }

	private void remoteConfigFailure(String msg){
		try {
            String localData = FirestoreManager.getInstance().getStringSharedpreference(REApplication.getAppContext(), REMOTE_DATA);
            String version = FirestoreManager.getInstance().getStringSharedpreference(REApplication.getAppContext(), REMOTE_VERSION);

            if(localData.isEmpty()||version.isEmpty()||version.equalsIgnoreCase("0.0")||Integer.parseInt(version)< Integer.parseInt(getApplicationContext().getString(R.string.remote_config_version))){
                Map<String, String> xmlDefaults = DefaultsXmlParser.getDefaultsFromXml(getApplicationContext(), R.xml.remote_config_defaults);
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                final RemoteConfigData  defaultData= mapper.convertValue(xmlDefaults, RemoteConfigData.class);
              //  REApplication.getInstance().setRemoteConfigData(defaultData);
                saveToSharedPreference("0.0", defaultData);
            }

            generateLogs(null, REFirestoreConstants.REMOTE_CONFIG, msg);


        } catch (Exception e) {
        }
	}
    public void getRemoteConfigDataFromFirestore(String id) {
		long startTime=System.currentTimeMillis();
        REApplication.mFireStoreInstance.collection(REMOTE_CONFIG).document("data")
                .get().addOnSuccessListener(documentSnapshot -> {
            try {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    RemoteConfigData data = documentSnapshot.toObject(RemoteConfigData.class);
                    saveToSharedPreference(id,data);
					Log.e("REMOTECONFIG","SET FIRST");
                 //   REApplication.getInstance().setRemoteConfigData(data);
					REUtils.sendResponseLog("Remote config data Firebase", "", (double)(System.currentTimeMillis()-startTime) / 1000);

				}
                else {
                 	remoteConfigFailure("No document Exist");
                }
            } catch (Exception e) {
             //   generateLogs(null, REFirestoreConstants.REMOTE_CONFIG, e.getMessage() != null ? e.getMessage() : "");
				remoteConfigFailure(e.getMessage());

            }
        }).addOnFailureListener(e ->
        {
            FirebaseCrashlytics.getInstance().recordException(e);
           remoteConfigFailure(e.getMessage());
        });

    }

    public void saveToSharedPreference(String id, RemoteConfigData data) {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = REApplication.getAppContext().getSharedPreferences("REMOTE_CONFIG_SHARED",MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        String json = new Gson().toJson(data);

       // Storing the key and its value as the data fetched from edittext
        myEdit.putString(REMOTE_VERSION, id);
        myEdit.putString(REMOTE_DATA, json);
       myEdit.putString(APP_VERSION_DATA, BuildConfig.VERSION_CODE+"");
       // Once the changes have been made,
      // we need to commit to apply those changes made,
     // otherwise, it will throw an error
        myEdit.commit();
    }
    public String getStringSharedpreference(Context context,String keyName){
        SharedPreferences sh = context.getSharedPreferences("REMOTE_CONFIG_SHARED", MODE_PRIVATE);

        String strData = sh.getString(keyName, "");
        return strData;
    }


    public void getHtmlData(Context context,String country){
        REApplication.mFireStoreInstance.collection(REALTIME_DB).document(STATIC_REALTIME_DB).collection(country).document("data").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> ewMap = (Map<String, Object>) document.get("extended-warranty");
                        String ewUrl = ewMap.get("url").toString();

                        Map<String, Object> faqMap = (Map<String, Object>) document.get("faqs");
                        String faqUrl = faqMap.get("url").toString();

                        Map<String, Object> rsaMap = (Map<String, Object>) document.get("rsa");
                        String rsaUrl = rsaMap.get("url").toString();

                        Map<String, Object> createRideTermMap = (Map<String, Object>) document.get("create-ride-terms");
                        String createRideTermUrl = createRideTermMap.get("url").toString();

                        Map<String, Object> tncMap = (Map<String, Object>) document.get("terms-and-conditions");
                        String tncUrl = tncMap.get("url").toString();

                        Map<String, Object> ppMap = (Map<String, Object>) document.get("privacy-policy");
                        String ppUrl = ppMap.get("url").toString();

                        Map<String, Object> diyMap = (Map<String, Object>) document.get("diyVideos");
                        String diyContainerUrl = diyMap.get("containerName").toString();
                        String diyDir = diyMap.get("dirName").toString();
                        String diyFile = diyMap.get("fileName").toString();

                        Map<String, Object> tncDRSAMap = (Map<String, Object>) document.get("terms-and-conditions-drsa");
                        String tncDRSAUrl = tncDRSAMap.get("url").toString();
                        REPreference.getInstance().putString(context, REConstants.KEY_PRIVACY, ppUrl);
                        REPreference.getInstance().putString(context, REConstants.KEY_EW, ewUrl);
                        REPreference.getInstance().putString(context, REConstants.KEY_RSA, rsaUrl);
                        REPreference.getInstance().putString(context, REConstants.KEY_FAQ, faqUrl);
                        REPreference.getInstance().putString(context, REConstants.KEY_RTC, createRideTermUrl);
                        REPreference.getInstance().putString(context, REConstants.KEY_TNC, tncUrl);
                        REPreference.getInstance().putString(context, REConstants.KEY_TNC_DRSA, tncDRSAUrl);
                        REPreference.getInstance().putString(context, REConstants.KEY_DIY_CONTAINER, diyContainerUrl);
                        REPreference.getInstance().putString(context, REConstants.KEY_DIY_DIRNAME, diyDir);
                        REPreference.getInstance().putString(context, REConstants.KEY_DIY_FILENAME, diyFile);
                    }
                    else{
                        if (!country.equalsIgnoreCase("default"))
                            getHtmlData(context, "default");
                    }
                }
                catch (Exception e){
                //    e.printStackTrace();
                    generateLogs(null, STATIC_REALTIME_DB, e.getMessage() != null ? e.getMessage() : "");
                }
            } else {
                FirebaseCrashlytics.getInstance().recordException(task.getException());
                generateLogs(null, STATIC_REALTIME_DB, task.getException()!= null ? task.getException().getMessage() : "");
            }
        }).addOnFailureListener(e->{
            FirebaseCrashlytics.getInstance().recordException(e);
        });

    }


    public void getStateMasterFirestore(String country, final MasterDataCallback stateandCityResponseCallback){
        REApplication.mFireStoreInstance.collection(REALTIME_DB).document(STATE_REALTIME_DB).collection(country).document("data").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> groups = (List<Map<String, Object>>) document.get("list");
                        ArrayList<StateModel> stateData = new ArrayList<>();
                        for (Map<String, Object> group : groups) {
                            String code = group.get("code").toString();
                            String countryCode = group.get("countrycode").toString();
                            String description = group.get("description").toString();
                            stateData.add(new StateModel(code,countryCode,description));
                        }
                        if (stateandCityResponseCallback != null)
                            stateandCityResponseCallback.onStateDataObtained(stateData);
                    }
                    else{
                        generateLogs(null, STATE_REALTIME_DB,  "Document not exist");

                    }
                }
                else{
                    FirebaseCrashlytics.getInstance().recordException(task.getException());
                    generateLogs(null, STATE_REALTIME_DB, task.getException()!= null ? task.getException().getMessage() : "");

                }
            }
        }).addOnFailureListener(e->{
            FirebaseCrashlytics.getInstance().recordException(e);
        });
    }

    public void getCityMasterFirestore(String countryCode,String cityCode,MasterDataCallback mCallback){
        REApplication.mFireStoreInstance.collection(REALTIME_DB).document(CITY_REALTIME_DB).collection(countryCode).document(cityCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> groups = (List<Map<String, Object>>) document.get("list");
                        ArrayList<CityModel> cityData = new ArrayList<>();
                        for (Map<String, Object> group : groups) {

                            String code = group.get("id").toString();
                            String description = group.get("description").toString();
                            CityModel cityModel= new CityModel(description);
                            cityModel.setmID(code);
                            cityData.add(cityModel);
                        }
                        if(mCallback!=null)
                            mCallback.onCityDataObtained(cityData);
                    }
                    else{
                        generateLogs(null, CITY_REALTIME_DB, "Document not exist");

                    }
                }
                else{
                    FirebaseCrashlytics.getInstance().recordException(task.getException());
                    generateLogs(null, CITY_REALTIME_DB, task.getException() != null ? task.getException().getMessage() : "");

                }
            }
        }).addOnFailureListener(e->{
            FirebaseCrashlytics.getInstance().recordException(e);
        });
    }


    public void getCountryDataFromAzure(){
        String urlCountry = REUtils.getMobileappbaseURLs().getCountryUrl();
        Log.e("URLCOUNTRY",urlCountry);
        try {
            String oldUrl = REPreference.getInstance().getString(REApplication.getAppContext(), COUNTRY_URL_KEY);
            if (oldUrl != null && oldUrl.length() > 0) {
                if (!oldUrl.equalsIgnoreCase(urlCountry))
                    getCountryListJson(urlCountry);
            } else {

                getCountryListJson(urlCountry);
            }
        } catch (PreferenceException e) {
            RELog.e(e);
        }
    }

}
