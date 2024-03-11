package com.royalenfield.reprime.ui.home.service.asynctask;

import android.os.AsyncTask;

import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.home.service.search.adapter.DistanceCalculationHelper;

import java.util.List;

public class DealersDistanceCalcAsyncTask extends AsyncTask<Void, Void, List<DealerMasterResponse>> {

    private List<DealerMasterResponse> mDealersList;
    private ServiceAsyncTaskListeners.DealerDistanceCalc mListener;

    public DealersDistanceCalcAsyncTask(List<DealerMasterResponse> mDealersList,
                                        ServiceAsyncTaskListeners.DealerDistanceCalc listener) {
        this.mDealersList = mDealersList;
        this.mListener = listener;
    }

    @Override
    protected List<DealerMasterResponse> doInBackground(Void... voids) {
        mDealersList = DistanceCalculationHelper.getInstances().getDistanceBetweenServiceCenter(mDealersList,
                true);
        return mDealersList;
    }

    @Override
    protected void onPostExecute(List<DealerMasterResponse> dealersResponses) {
        mListener.onDealersDistanceCalcComplete(dealersResponses);
    }
}
