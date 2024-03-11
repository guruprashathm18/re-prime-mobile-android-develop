package com.royalenfield.reprime.ui.home.service.diy.interactor;

import android.annotation.SuppressLint;
import android.util.Log;
import com.royalenfield.reprime.ui.home.service.diy.listner.YoutubeCallbackListener;
import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchYoutubeItemsInteractor implements IFetchYoutubeItemsInteractor {
    private static final String TAG = "FetchYoutubeItemsInteractor";

    @Override
    public void getVideoAttributes(String videoIds, final YoutubeCallbackListener mYoutubeCallbacklistner) {
        //  @GET("id=7lCDEYXw3mM&key=YOUR_API_KEY&fields=items(id,snippet(channelId,title,categoryId),statistics)&part=snippet,statistics")
        REApplication.getInstance().
                getREYoutubeAPI()
                .getYoutubeAPI()
                .getVideoAttributes(videoIds, REUtils.getREGoogleKeys().getYoutubeaPIKey(), "items(id,snippet(channelId,title,categoryId),statistics)", "snippet,statistics").enqueue(new Callback<VideoAttribute>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<VideoAttribute> call, Response<VideoAttribute> response) {
                Log.d(TAG, "Response is," + response.code());
                if (response.isSuccessful()) {
                    mYoutubeCallbacklistner.onSuccess(response.body());
                } else {
                    Log.d(TAG, "Response is," + response.isSuccessful());
                    mYoutubeCallbacklistner.onFailure(response.message());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<VideoAttribute> call, Throwable t) {
                Log.d(TAG, "onFailure," + t.getMessage());

            }
        });
    }
}
