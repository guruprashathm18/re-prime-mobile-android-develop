package com.royalenfield.reprime.rest.googlemaps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royalenfield.reprime.utils.REConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class REGoogleMapAPI {

    private Retrofit retrofit = null;

    /**
     * This method creates a new instance of the RE Website API interface.
     *
     * @return The API interface
     */
    public GoogleMapNetworkInterface getGoogleMapAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(REConstants.RE_GOOGLEMAP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(GoogleMapNetworkInterface.class);
    }
}
