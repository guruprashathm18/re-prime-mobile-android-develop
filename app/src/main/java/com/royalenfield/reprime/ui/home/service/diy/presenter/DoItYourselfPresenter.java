package com.royalenfield.reprime.ui.home.service.diy.presenter;

import android.util.Base64;

import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;
import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;
import com.royalenfield.reprime.ui.home.service.diy.interactor.DoItYourselfInteractor;
import com.royalenfield.reprime.ui.home.service.diy.listner.FilesCallbackListener;
import com.royalenfield.reprime.ui.home.service.diy.listner.YoutubeCallbackListener;
import com.royalenfield.reprime.ui.home.service.diy.view.DoItYourselfView;
import com.royalenfield.reprime.utils.REUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.royalenfield.reprime.utils.RELog;

public class DoItYourselfPresenter implements IDoItYourselfPresenter, YoutubeCallbackListener, FilesCallbackListener {
    private final String TAG = "DoItYourselfPresenter";
    private DoItYourselfView mDoItYourselfView;
    private DoItYourselfInteractor mDoItYourselfInteractor;

    public DoItYourselfPresenter(DoItYourselfView mDoItYourselfView, DoItYourselfInteractor mDoItYourselfInteractor) {
        this.mDoItYourselfView = mDoItYourselfView;
        this.mDoItYourselfInteractor = mDoItYourselfInteractor;
    }

    @Override
    public void fetchVideoList(VideoAttribute mVideoAttribute) {
        ArrayList<YoutubeVideoModel> mYoutubeVideoModelList = mDoItYourselfInteractor.generateDummyVideoList(mVideoAttribute);
        if (mYoutubeVideoModelList != null && mYoutubeVideoModelList.size() > 0) {
            mDoItYourselfView.setDataToRecyclerView(mYoutubeVideoModelList);
        }
    }

    @Override
    public void fetchVideoAttributes(String[] strArray) {
        // String[] videoId_array = REApplication.getInstance().getResources().getStringArray(R.array.video_id_array);
        String videoIds = REUtils.convertArrayToString(strArray);
        mDoItYourselfView.showProgress();
        mDoItYourselfInteractor.getVideoAttributes(videoIds, this);
    }

    @Override
    public void downloadFileFromAzure(String containerName,
                                      String dirName, String fileName) {
        mDoItYourselfView.showProgress();
        mDoItYourselfInteractor.downloadFilesFromAzure(this, containerName, dirName, fileName);
    }

    @Override
    public void onSuccess(VideoAttribute mVideoAttribute) {
        mDoItYourselfView.hideProgress();
        mDoItYourselfView.onFetchSuccess(mVideoAttribute);
    }

    @Override
    public void onSuccess(String mJsonData) {
        String jsonData = "jsonData";
        try {
            byte[] data = Base64.decode(mJsonData, Base64.DEFAULT);
            jsonData = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            RELog.e(e);
        }
        RELog.e(DoItYourselfPresenter.class.getSimpleName(),"JSON:"+ jsonData);
        mDoItYourselfView.hideProgress();
        mDoItYourselfView.onVideosFetchSuccess(jsonData);
    }

    @Override
    public void onFailure(String errorMessage) {
        mDoItYourselfView.hideProgress();
        mDoItYourselfView.onResponseFailure(errorMessage);
    }
}
