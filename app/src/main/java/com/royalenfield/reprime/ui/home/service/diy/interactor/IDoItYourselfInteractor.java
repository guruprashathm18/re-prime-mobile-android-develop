package com.royalenfield.reprime.ui.home.service.diy.interactor;

import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;
import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;
import com.royalenfield.reprime.ui.home.service.diy.listner.FilesCallbackListener;
import com.royalenfield.reprime.ui.home.service.diy.listner.YoutubeCallbackListener;

import java.util.ArrayList;

public interface IDoItYourselfInteractor {

    ArrayList<YoutubeVideoModel> generateDummyVideoList(VideoAttribute mVideoAttribute);

    void getVideoAttributes(String videoId, YoutubeCallbackListener mYoutubeCallbacklistner);

    void downloadFilesFromAzure(FilesCallbackListener filesCallbackListener, String containerName,
                                String dirName, String fileName);
}
