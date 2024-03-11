package com.royalenfield.reprime.ui.home.service.diy.interactor;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.diyyoutube.Item;
import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;
import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;
import com.royalenfield.reprime.ui.home.service.diy.listner.FilesCallbackListener;
import com.royalenfield.reprime.ui.home.service.diy.listner.YoutubeCallbackListener;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoItYourselfInteractor implements IDoItYourselfInteractor {
    private static final String TAG = DoItYourselfInteractor.class.getSimpleName();

    @Override
    public ArrayList<YoutubeVideoModel> generateDummyVideoList(VideoAttribute mVideoAttribute) {
        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();
        //loop through all items and add them to array list
        List<Item> itemList = mVideoAttribute.getItems();
        for (Item item : itemList) {
            YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
            youtubeVideoModel.setVideoId(item.getId());
            youtubeVideoModel.setLikes(item.getStatistics().getLikeCount());
            youtubeVideoModel.setTitle(item.getSnippet().getTitle());
            youtubeVideoModelArrayList.add(youtubeVideoModel);
        }
        return youtubeVideoModelArrayList;

    }

    @Override
    public void getVideoAttributes(String videoIds, final YoutubeCallbackListener mYoutubeCallbacklistner) {
        REApplication.getInstance().
                getREYoutubeAPI()
                .getYoutubeAPI()
                .getVideoAttributes(videoIds, REUtils.getREGoogleKeys().getYoutubeaPIKey(), "items(id,snippet(channelId,title,categoryId),statistics)",
                        "snippet,statistics").enqueue(new Callback<VideoAttribute>() {

            @Override
            public void onResponse(Call<VideoAttribute> call, Response<VideoAttribute> response) {
                Log.d(TAG, "Response is," + response.code());
                if (response.isSuccessful()) {
                    mYoutubeCallbacklistner.onSuccess(response.body());
                } else {
                    Log.d(TAG, "Response is," + false);
                    mYoutubeCallbacklistner.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<VideoAttribute> call, Throwable t) {
                Log.d(TAG, "onFailure," + t.getMessage());
                if (t instanceof NetworkErrorException) {
                    mYoutubeCallbacklistner.onFailure(REApplication.getAppContext().
                            getResources().getString(R.string.network_failure));
                } else {
                    mYoutubeCallbacklistner.onFailure(t.getMessage());
                }
            }
        });
    }

    @Override
    public void downloadFilesFromAzure(FilesCallbackListener filesCallbackListener, String containerName,
                                       String dirName, String fileName) {
        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("containerName", containerName)
                .addFormDataPart("dirName", dirName)
                .addFormDataPart("fileName", fileName);
        RequestBody requestBody = multipartBody.build();
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getAzureFileAPI()
                .downloadFilesFromAzure("4", requestBody)
                .enqueue(new Callback<AzureFileDownloadResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<AzureFileDownloadResponse> call, @NotNull Response<AzureFileDownloadResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (!response.body().isError()) {
                                filesCallbackListener.onSuccess(response.body().getData());
                            } else {
                                filesCallbackListener.onFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            // error case
                            filesCallbackListener.onFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<AzureFileDownloadResponse> call, @NotNull Throwable t) {
                        filesCallbackListener.onFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}
