package com.royalenfield.reprime.ui.riderprofile.interactor;

import com.royalenfield.reprime.base.REMvpView;
import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;

public interface IModifyRideCreator {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     * */
    interface presenter{

        void callModifyRideDetail(CreateRideRequest createRideRequest, String imgFilePath);
    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    interface MainView extends REMvpView {

        void onModifyRideSuccess();

        void onResponseFailure(String throwable);
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface ModifyRideDetailsIntractor {

        interface ModifyRideDetailsListener {
            void onModifyRideDetailsSuccess();

            void onModifyRideDetailsFailure(String failureMsg);
        }

        void modifyRideDetails(CreateRideRequest createRideRequest, String imgFilePath, ModifyRideDetailsListener rideDetailsListener);
    }
}