package com.royalenfield.reprime.ui.home.service.asynctask;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.utils.REConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Dealer data async task for getting dealer data/detail
 */
public class DealerDataAsyncTask extends AsyncTask<Void, Void, Void> {

    private List<DealerMasterResponse> mDealersList = new ArrayList<>();
    private DealerMasterResponse mDealerDetail;
    private String mDealerBranchId;
    private DataSnapshot mDataSnapshot;
    private ServiceAsyncTaskListeners.DealerDataAsyncTaskCompleteListener mListener;

    public DealerDataAsyncTask(DataSnapshot dataSnapshot, String branchId,
                               ServiceAsyncTaskListeners.DealerDataAsyncTaskCompleteListener listener) {
        mDataSnapshot = dataSnapshot;
        mDealerBranchId = branchId;
        mListener = listener;
    }

    /**
     * Forms dealers List from DocumentSnapshot
     *
     * @param dataSnapshot : DataSnapshot
     * @return : List<DealersResponse>
     */
    private List<DealerMasterResponse> getDealerListFromSnapshot(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            DealerMasterResponse dealersResponse = postSnapshot.getValue(DealerMasterResponse.class);
            mDealersList.add(dealersResponse);
        }
        return mDealersList;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mDealersList = getDealerListFromSnapshot(mDataSnapshot);
        if (mDealerBranchId != null) {
            if (!mDealerBranchId.isEmpty()) {
                //Filter for getting dealer data from dealerList
                for (DealerMasterResponse dealersDetail : mDealersList) {
                    if (dealersDetail.getBranchTypeIdentifier() != null && dealersDetail.getBranchTypeIdentifier().equals(REConstants.BRANCH_TYPE_IDENTIFIER_FIREBASE)) {
                        mDealerDetail = dealersDetail;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mDealerBranchId.isEmpty() && mListener != null) {
            mListener.onDealerListSuccess(mDealersList);
        } else {
            if (mListener != null) {
                mListener.onDealerDetailSuccess(mDealerDetail);
            }
        }
    }
}
