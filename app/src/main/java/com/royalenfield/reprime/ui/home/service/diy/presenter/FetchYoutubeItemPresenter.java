package com.royalenfield.reprime.ui.home.service.diy.presenter;

import com.royalenfield.reprime.ui.home.service.diy.view.FetchYoutubeItemView;
import com.royalenfield.reprime.ui.home.service.diy.interactor.FetchYoutubeItemsInteractor;

public class FetchYoutubeItemPresenter {
    private FetchYoutubeItemView mFetchYoutubeItemView;
    private FetchYoutubeItemsInteractor mFetchYoutubeItemsInteractor;

    public FetchYoutubeItemPresenter(FetchYoutubeItemView mFetchYoutubeItemView, FetchYoutubeItemsInteractor mfetchYoutubeItemsInteractor) {
        this.mFetchYoutubeItemView = mFetchYoutubeItemView;
        this.mFetchYoutubeItemsInteractor = mfetchYoutubeItemsInteractor;
    }
}
