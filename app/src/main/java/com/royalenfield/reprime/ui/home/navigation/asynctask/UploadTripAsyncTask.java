package com.royalenfield.reprime.ui.home.navigation.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.bosch.softtec.components.nephele.CloudException;
import com.bosch.softtec.micro.tenzing.client.model.ShareTicket;
import com.bosch.softtec.micro.tenzing.client.model.ShareTicketBody;
import com.bosch.softtec.micro.tenzing.client.model.TripBodyWithImageUrl;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.utils.REConstants;

import java.math.BigDecimal;

import com.royalenfield.reprime.utils.RELog;

/**
 * Async task for creating tripId for BCT
 */
public class UploadTripAsyncTask extends AsyncTask<String, Void, String> {

    private TripBodyWithImageUrl mTripBody;
    private CloudException exception;
    private NavigationAsyncTaskListeners.UploadTripListener mListener;
    private int mActionMode;
    private int index;
    private boolean mIsShare = false;


    public UploadTripAsyncTask(NavigationAsyncTaskListeners.UploadTripListener listener, TripBodyWithImageUrl tripBody,
                                int actionMode,int indexTrip) {
        mListener = listener;
        mTripBody = tripBody;
        mActionMode = actionMode;
        index=indexTrip;

    }

    @Override
    protected String doInBackground(String... params) {
        String strTripId = "";
        if (params != null && params.length > 0 && mActionMode == REConstants.RE_SHARE) {
            strTripId = params[0];
        }
        try {
            if (mActionMode == REConstants.SAVE) {
                mIsShare = false;
                strTripId = uploadTripBodyandImage();
            } else if (mActionMode == REConstants.SHARE) {
                mIsShare = true;
                strTripId = uploadTripBodyandImage();
                strTripId = shareTrip(strTripId);
            } else if (mActionMode == REConstants.RE_SHARE) {
                mIsShare = true;
                strTripId = shareTrip(strTripId);
            }

        } catch (CloudException e) {
            exception = e;
            RELog.e(e);
            Log.e("Uploadtrip", " :" + e.getHttpErrorMessage());
            return e.getHttpErrorMessage();
        }
        return strTripId;
    }

    @Override
    protected void onPostExecute(String result) {
        if (exception == null) {
            if (result != null && !result.equals("") && result.length() > 0) {
                if (!result.equalsIgnoreCase("timeout")) {
                    mListener.onUploadTripSuccess(result, mIsShare,index);
                } else {
                    mListener.onUploadTripFailure(result,index);
                }
            } else {
                mListener.onUploadTripFailure(result, index);
            }
        } else {
            mListener.onUploadTripFailure(exception.getHttpErrorMessage(), index);
        }
    }

    private String uploadTripBodyandImage() {
        String strTripId = REApplication.getInstance().getCloudManager().uploadTrip(mTripBody);
        Log.e("uploadTripBodyandImage", "TripId:" + strTripId);
        return strTripId;
    }

    private String shareTrip(String strTripId) {
        ShareTicketBody shareTicketBody = new ShareTicketBody();
        shareTicketBody.setTripId(strTripId);
        BigDecimal bigDecimal = new BigDecimal(345600);
        shareTicketBody.setExpiry(bigDecimal);
        ShareTicket strSharedToken = REApplication.getInstance().getCloudManager().shareTrip(shareTicketBody);
        Log.e("test", "share trip code" + strSharedToken.getCode());
        Log.e("test", "share trip strTripId" + strTripId);
        return strSharedToken.getCode();
    }
}
