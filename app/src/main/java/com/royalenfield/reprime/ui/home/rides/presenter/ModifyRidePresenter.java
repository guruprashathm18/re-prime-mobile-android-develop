package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.ui.riderprofile.interactor.IModifyRideCreator;

public class ModifyRidePresenter implements IModifyRideCreator.presenter,
        IModifyRideCreator.ModifyRideDetailsIntractor.ModifyRideDetailsListener {

    private IModifyRideCreator.ModifyRideDetailsIntractor mModifyRideInteractor;
    private IModifyRideCreator.MainView mModifyRideView;

    public ModifyRidePresenter(IModifyRideCreator.MainView mModifyRideView,
                               IModifyRideCreator.ModifyRideDetailsIntractor mModifyRideInteractor) {
        this.mModifyRideInteractor = mModifyRideInteractor;
        this.mModifyRideView = mModifyRideView;
    }

    public void onDestroy() {
        mModifyRideView = null;
    }

    @Override
    public void onModifyRideDetailsSuccess() {
        if (mModifyRideView != null) {
            mModifyRideView.onModifyRideSuccess();
            mModifyRideView.hideLoading();
        }
    }

    @Override
    public void onModifyRideDetailsFailure(String failureMsg) {
        if (mModifyRideView != null) {
            mModifyRideView.onResponseFailure(failureMsg);
            mModifyRideView.hideLoading();
        }
    }

    @Override
    public void callModifyRideDetail(CreateRideRequest createRideRequest, String imgFilePath) {
        mModifyRideView.showLoading();
        // This is for api call
        mModifyRideInteractor.modifyRideDetails(createRideRequest, imgFilePath, this);
    }
}
