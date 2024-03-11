package com.royalenfield.reprime.ui.home.service.diy.interactor;

import com.royalenfield.reprime.ui.home.service.diy.view.DoItYourSelfRowView;
import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;

import java.util.List;

public interface IDoItYourselfRowPresenter {

    /**
     * for binding data to adapter rows .
     *
     * @param position
     * @param mDoItYourSelfRowView
     * @param mYoutubeVideoModelList
     */
    void onBindDoItYourselfRow(int position, DoItYourSelfRowView mDoItYourSelfRowView, List<YoutubeVideoModel> mYoutubeVideoModelList);


    /**
     * Returns size of mYoutubeVideoModelList
     *
     * @param mYoutubeVideoModelList
     * @return size of mYoutubeVideoModelList
     */
    int getItemCount(List<YoutubeVideoModel> mYoutubeVideoModelList);


}
