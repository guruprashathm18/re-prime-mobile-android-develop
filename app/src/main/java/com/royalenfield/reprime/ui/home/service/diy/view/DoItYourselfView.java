package com.royalenfield.reprime.ui.home.service.diy.view;

import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;
import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;

import java.util.ArrayList;

public interface DoItYourselfView {

    void showProgress();

    void hideProgress();

    void setDataToRecyclerView(ArrayList<YoutubeVideoModel> mYoutubeVideoModel);

    void onResponseFailure(String message);

    void onFetchSuccess(VideoAttribute mVideoAttribute);

    void onVideosFetchSuccess(String mVideosData);

}
