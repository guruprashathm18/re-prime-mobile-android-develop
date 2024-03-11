package com.royalenfield.reprime.rest.youtube;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royalenfield.reprime.utils.REConstants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class REYoutubeAPI {
    private Retrofit retrofit = null;

    /**
     * This method creates a new instance of the RE Youtube API interface.
     *
     * @return The API interface
     */
    public YoutubeNetworkServiceInterface getYoutubeAPI() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(REConstants.RE_YOUTUBE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(YoutubeNetworkServiceInterface.class);
    }
}
