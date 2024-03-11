package com.royalenfield.firebase.realTimeDatabase;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.home.service.asynctask.DealerDataAsyncTask;
import com.royalenfield.reprime.ui.home.service.asynctask.ServiceAsyncTaskListeners;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.generateLogs;

public class FirebaseManager {

    private static FirebaseManager mFirebaseManagerInstance;
    private DatabaseReference databaseReference;
    private String mDealerBranchId;

    /**
     * Returns the application instance.
     *
     * @return {@link Application} instance.
     */
    public static FirebaseManager getInstance() {
        if (mFirebaseManagerInstance == null) {
            mFirebaseManagerInstance = new FirebaseManager();
        }

        return mFirebaseManagerInstance;
    }

    /**
     * Retrieve an instance of your database using getInstance() and reference the location you want to read/write to.
     *
     * @param dealerListResponseCallback {@link OnFirebaseDealerResponseCallback}
     */
    public void fetchDealerResponseFromFirebase(String dealerId,
                                                final OnFirebaseDealerResponseCallback dealerListResponseCallback) {
        try {
            Log.d("API", "Firebase call for dealers :" + dealerId);
            mDealerBranchId = dealerId;
            prepareDealersList(dealerListResponseCallback);
        } catch (IllegalArgumentException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    /**
     * The onDataChange() method in this class is triggered once when the listener is attached and again
     * every time the data changes, including the children.
     *
     * @param onDataReceiveCallback {@link OnFirebaseDealerResponseCallback}
     */
    private void prepareDealersList(final OnFirebaseDealerResponseCallback onDataReceiveCallback) {
        DatabaseReference dealerMasterReference = FirebaseDatabase
                .getInstance(REUtils.getFirebaseURLs().getDealerMasterURL()).getReference();
        dealerMasterReference.keepSynced(true);
        Query query = null;
        //if branchCode is available get details of that dealer
        if (mDealerBranchId != null && !mDealerBranchId.isEmpty()) {
            query = dealerMasterReference.child("CRMCORE").child("IN").child("dealers").
                    orderByChild("branchCode").equalTo(mDealerBranchId);
        } else {
            query = dealerMasterReference.child("CRMCORE").child("IN").child("dealers").
                    orderByChild("branchTypeIdentifier").equalTo("Service");
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DealerDataAsyncTask dealerDataAsyncTask =
                        new DealerDataAsyncTask(dataSnapshot, mDealerBranchId,
                                new ServiceAsyncTaskListeners.DealerDataAsyncTaskCompleteListener() {
                                    @Override
                                    public void onDealerListSuccess(List<DealerMasterResponse> dealersList) {
                                        if (dealersList != null && dealersList.size() > 0) {
                                            onDataReceiveCallback.onFirebaseDealersListSuccess(dealersList);
                                        } else {
                                            onDataReceiveCallback.onFirebaseDealerListFailure("");
                                        }
                                    }

                                    @Override
                                    public void onDealerDetailSuccess(DealerMasterResponse dealersResponse) {
                                        if (onDataReceiveCallback != null && dealersResponse != null) {
                                            onDataReceiveCallback.onFirebaseDealerDetailSuccess(dealersResponse);
                                        } else {
                                            if (onDataReceiveCallback != null)
                                                onDataReceiveCallback.onFirebaseDealerDetailFailure("");
                                        }
                                    }
                                });
                dealerDataAsyncTask.execute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.e("API", "Firebase error while fetching dealers:" + databaseError.getMessage());
                generateLogs(null, "Dealer Master data", databaseError.getMessage());
                if (mDealerBranchId.isEmpty()) {
                    if (onDataReceiveCallback != null)
                        onDataReceiveCallback.onFirebaseDealerListFailure(databaseError.getMessage());
                } else {
                    if (onDataReceiveCallback != null)
                        onDataReceiveCallback.onFirebaseDealerDetailFailure(databaseError.getMessage());
                }
            }

        });
    }
}