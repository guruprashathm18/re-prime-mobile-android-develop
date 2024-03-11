package com.royalenfield.reprime.ui.home.service.diy.listner;

import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;

public interface YoutubeCallbackListener {

    void onSuccess(VideoAttribute mVideoAttribute);

    void onFailure(String errorMessage);


}
