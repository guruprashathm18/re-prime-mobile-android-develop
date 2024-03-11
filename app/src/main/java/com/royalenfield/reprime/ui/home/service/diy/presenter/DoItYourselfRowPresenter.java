package com.royalenfield.reprime.ui.home.service.diy.presenter;

import android.view.View;
import com.royalenfield.reprime.ui.home.service.diy.view.DoItYourSelfRowView;
import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;
import com.royalenfield.reprime.ui.home.service.diy.interactor.IDoItYourselfRowPresenter;

import java.util.List;

public class DoItYourselfRowPresenter implements IDoItYourselfRowPresenter {

    /**
     * for binding data to adapter row.
     *
     * @param position
     * @param mDoItYourSelfRowView
     * @param mYoutubeVideoModelList
     */
    @Override
    public void onBindDoItYourselfRow(int position, DoItYourSelfRowView mDoItYourSelfRowView, List<YoutubeVideoModel> mYoutubeVideoModelList) {
        initializeThumbnailVies(position, mDoItYourSelfRowView, mYoutubeVideoModelList);
    }

    /**
     * returns YoutubeVideoModel list size.
     *
     * @param mYoutubeVideoModelList
     * @return YoutubeVideoModel list size
     */
    @Override
    public int getItemCount(List<YoutubeVideoModel> mYoutubeVideoModelList) {
        return mYoutubeVideoModelList != null ? mYoutubeVideoModelList.size() : 0;
    }

    /**
     * initializing adapter row with youtube video attributes like, video like count,video title
     *
     * @param position
     * @param mDoItYourSelfRowView
     * @param mYoutubeVideoModelList
     */
    void initializeThumbnailVies(int position, DoItYourSelfRowView mDoItYourSelfRowView, List<YoutubeVideoModel> mYoutubeVideoModelList) {
        if (mDoItYourSelfRowView != null && mYoutubeVideoModelList.size() > 0) {
            YoutubeVideoModel mYoutubeVideoModel = mYoutubeVideoModelList.get(position);
            if (mYoutubeVideoModel != null) {
                mDoItYourSelfRowView.setProgressBar(View.VISIBLE);
                mDoItYourSelfRowView.setLikesVisible(View.GONE);
                mDoItYourSelfRowView.setVideoTitleVisible(View.GONE);
                mDoItYourSelfRowView.setVideoTitle(mYoutubeVideoModel.getTitle());
                mDoItYourSelfRowView.setLikes(mYoutubeVideoModel.getLikes() + "Likes");
                mDoItYourSelfRowView.setImageBtnVisible(View.GONE);
                mDoItYourSelfRowView.initializeYoutubeThumbNailView(mYoutubeVideoModel);
            }
        }
    }
}