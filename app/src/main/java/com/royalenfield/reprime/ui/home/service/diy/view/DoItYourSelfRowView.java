package com.royalenfield.reprime.ui.home.service.diy.view;

import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;

/**
 * DIY adapter row attributes
 */
public interface DoItYourSelfRowView {

    void setVideoTitle(String videoTitle);

    void setLikes(String likes);

    void setProgressBar(int i);

    void setLikesVisible(int i);

    void setVideoTitleVisible(int i);

    void setImageBtnVisible(int i);

    void initializeYoutubeThumbNailView(YoutubeVideoModel mYoutubeVideoModel);


}
