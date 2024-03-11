package com.royalenfield.reprime.ui.home.service.diy.presenter;

import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;

public interface IDoItYourselfPresenter {
    void fetchVideoList(VideoAttribute mVideoAttribute);

    void fetchVideoAttributes(String[] strArray);

    void downloadFileFromAzure(String containerName,
                               String dirName, String fileName);
}
