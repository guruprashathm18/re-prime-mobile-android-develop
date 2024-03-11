package com.royalenfield.reprime.ui.home.service.diy.interactor;

import com.royalenfield.reprime.ui.home.service.diy.listner.YoutubeCallbackListener;

public interface IFetchYoutubeItemsInteractor {

    void getVideoAttributes(String videoId, YoutubeCallbackListener mYoutubeCallbacklistner);
}
