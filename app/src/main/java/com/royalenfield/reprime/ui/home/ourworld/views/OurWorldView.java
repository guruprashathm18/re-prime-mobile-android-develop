package com.royalenfield.reprime.ui.home.ourworld.views;

import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.models.response.firestore.ourworld.NewsResponse;
import com.royalenfield.reprime.base.REMvpView;

import java.util.List;

/**
 * @author BOP1KOR on 4/1/2019.
 */
public interface OurWorldView extends REMvpView {

    void onOurWorldNewsSuccess(List<NewsResponse> mNewsResponseList);

    void onOurWorldNewsFailure(String errorMessage);

    void onOurWorldEventSuccess(List<EventsResponse> mNewsResponseList);

    void onOurWorldEventFailure(String throwable);

}
