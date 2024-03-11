package com.royalenfield.reprime.ui.home.service.diy.view;

import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;

import java.util.ArrayList;

public interface FetchYoutubeItemView {
    void showProgress();

    void hideProgress();

    void setDataToRecyclerView(ArrayList<VideoAttribute> mVideoAttribute);

    void onResponseFailure(Throwable throwable);

    void onItemClick(VideoAttribute mVideoAttribute);
}
