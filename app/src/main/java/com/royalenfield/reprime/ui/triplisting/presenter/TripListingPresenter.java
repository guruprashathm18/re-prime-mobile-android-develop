package com.royalenfield.reprime.ui.triplisting.presenter;
import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.motorcyclehealthalert.utils.HealthAlertUtils;
import com.royalenfield.reprime.ui.triplisting.response.GetActivitySummary;
import com.royalenfield.reprime.ui.triplisting.response.TripListingResponseModel;
import com.royalenfield.reprime.ui.triplisting.response.TripResponseCallback;
import com.royalenfield.reprime.ui.triplisting.view.ITripListingView;
import com.royalenfield.reprime.ui.triplisting.view.TripItemModel;
import com.royalenfield.reprime.utils.REUtils;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TripListingPresenter implements ITripListingPresenter, TripResponseCallback {

    private final ITripListingView mTripListingView;

    public TripListingPresenter(ITripListingView view) {
        mTripListingView = view;
    }

    @Override
    public void createConnectionForTripData() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        String pastDate = dateFormat.format(cal.getTime());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = dateTimeFormatter.format(LocalDateTime.now());
        Calendar calNow = Calendar.getInstance();
        if (!REApplication.CODE_STUB_DEMO) {
            buildSocketConnection(cal.getTimeInMillis()/1000+"", calNow.getTimeInMillis()/1000+"");
        } else {
            getStubbedData();
        }
    }

    public void getStubbedData() {
            TripListingResponseModel tripResponse = new Gson().fromJson(REUtils.loadJSONFromAsset(REApplication.getAppContext(),
                    "trip_list_data.json"), TripListingResponseModel.class);
            tripResponseSuccess(tripResponse);

    }

    @Override
    public void getTripDataWithFilter(String fromDate, String toDate) {
        if (!REApplication.CODE_STUB_DEMO) {
            REApplication.getInstance().getREConnectedAPI()
                    .sendForTripDetailEvent(this, fromDate, toDate);
        } else {
            getStubbedData();
        }

    }

    private void buildSocketConnection(String pastDate, String date) {
        REApplication.getInstance().getREConnectedAPI()
                .createConnectionForTripDetails(this, pastDate, date);
    }


    @Override
    public void tripResponseSuccess(TripListingResponseModel tripListingResponseModel) {
        mTripListingView.hideLoading();
        List<GetActivitySummary> getActivitySummaries = tripListingResponseModel.getData()
                .getGetActivitySummary();

        if (getActivitySummaries != null) {

            ArrayList<TripItemModel> tripItemModels = new ArrayList<>();

            for (GetActivitySummary getActivitySummary : getActivitySummaries) {
                TripItemModel tripItemModel = mapTripListingResponseModelToTripViewModel(getActivitySummary);
                tripItemModels.add(tripItemModel);
            }

            mTripListingView.updateTripCount(getActivitySummaries.size());
            mTripListingView.setDataOnView(tripItemModels);
        } else {
            mTripListingView.updateTripCount(0);
        }
    }

    private TripItemModel mapTripListingResponseModelToTripViewModel(GetActivitySummary getActivitySummary) {

        TripItemModel tripItemModel = new TripItemModel(getActivitySummary.getTripId(), getActivitySummary.getStartAddress()
                , getActivitySummary.getEndAddress(), getActivitySummary.getTotalDist() + ""
                , getActivitySummary.getFromTs()
                , getActivitySummary.getToTs(),getActivitySummary.getTripCreatedTs()
                , getActivitySummary.getStartLoc()
                , getActivitySummary.getEndLoc(), getActivitySummary.getTotalHaCount()
                , getActivitySummary.getTotalHbCount(), getActivitySummary.getTotalIdlingTime(),false,getActivitySummary.getTripType(),getActivitySummary.getTotalRunningTime());

        tripItemModel.setUnformattedStartTime(getActivitySummary.getFromTs());
        tripItemModel.setOverSpeeding(getActivitySummary.getOverSpeedCount());
        tripItemModel.setAverageSpeedVal(getActivitySummary.getAverageSpeed());
        tripItemModel.setmTopSpeed(getActivitySummary.getTopSpeed());
      //  tripItemModel.setTotalScore(getActivitySummary.getTotalScore());
        tripItemModel.setFromTime(getActivitySummary.getFromTs());
        tripItemModel.setToTime(getActivitySummary.getToTs());
        return tripItemModel;
    }
}
