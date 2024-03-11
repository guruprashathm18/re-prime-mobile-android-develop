package com.royalenfield.reprime.ui.home.rides.presenter;
import com.royalenfield.reprime.models.request.web.rides.AddCheckInRequest;
import com.royalenfield.reprime.ui.riderprofile.interactor.ICheckINRideCreator;


public class CheckInRidePresenter implements ICheckINRideCreator.presenter,
        ICheckINRideCreator.CheckInRideDetailsIntractor.CheckInRideDetailsListener {

    private ICheckINRideCreator.CheckInRideDetailsIntractor mCheckInRideInteractor;
    private ICheckINRideCreator.MainView mCheckInRideView;

    public CheckInRidePresenter(ICheckINRideCreator.MainView mModifyRideView,
                                ICheckINRideCreator.CheckInRideDetailsIntractor mModifyRideInteractor) {
        this.mCheckInRideInteractor = mModifyRideInteractor;
        this.mCheckInRideView = mModifyRideView;
    }

    public void onDestroy() {
        mCheckInRideView = null;
    }

    @Override
    public void onCheckInRideDetailsSuccess() {
        if (mCheckInRideView != null) {
            mCheckInRideView.onCheckInRideSuccess();
            mCheckInRideView.hideLoading();
        }
    }

    @Override
    public void onCheckInRideDetailsFailure(String failureMsg) {
        if (mCheckInRideView != null) {
            mCheckInRideView.onResponseFailure(failureMsg);
            mCheckInRideView.hideLoading();
        }
    }

    @Override
    public void checkInRideDetail(AddCheckInRequest addCheckInRequest, String imgFilePath) {
        mCheckInRideView.showLoading();
        // This is for api call
        mCheckInRideInteractor.checkInRideDetails(addCheckInRequest, imgFilePath, this);
    }
}
