package com.royalenfield.reprime.ui.riderprofile.interactor;

import com.royalenfield.reprime.base.REMvpView;
import com.royalenfield.reprime.models.request.web.rides.AddCheckInRequest;


public interface ICheckINRideCreator {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     * */
    interface presenter{

        void checkInRideDetail(AddCheckInRequest addCheckInRequest, String imgFilePath);
    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    interface MainView extends REMvpView {

        void onCheckInRideSuccess();

        void onResponseFailure(String throwable);
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface CheckInRideDetailsIntractor {

        interface CheckInRideDetailsListener {
            void onCheckInRideDetailsSuccess();

            void onCheckInRideDetailsFailure(String failureMsg);
        }

        void checkInRideDetails(AddCheckInRequest checkInRequest, String imgFilePath, CheckInRideDetailsListener checkInRideDetailsListener);
    }
}