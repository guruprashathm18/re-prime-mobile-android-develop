package com.royalenfield.reprime.rest.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.rest.web.RESSLClient;
import com.royalenfield.reprime.utils.REUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author BOP1KOR on 12/7/2018.
 * <p>
 * This class represents the RE DMS API service.
 */

public class REServiceAPI {
    private Retrofit retrofit = null;
    private Retrofit tbtRetrofit = null;
    private Retrofit firebaseTokenRetrofit = null;

    /**
     * This method creates a new instance of the RE DMS API interface.
     *
     * @return The API interface
     */
    public NetworkServiceInterface getServiceApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getAppBaseURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), true))
                    .build();
        }
        return retrofit.create(NetworkServiceInterface.class);
    }

    /**
     * This method creates a new instance of the RE TBT API interface.
     *
     * @return The API interface
     */
    public NetworkServiceInterface getTBTApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (tbtRetrofit == null) {
            tbtRetrofit = new Retrofit
                    .Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getTbtURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), true))
                    .build();
        }
        return tbtRetrofit.create(NetworkServiceInterface.class);
    }
    /**
     * This method creates a new instance of the RE Firebasetoken API interface.
     *
     * @return The API interface
     */
    public NetworkServiceInterface getFirebaseTokenApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (firebaseTokenRetrofit == null) {
            firebaseTokenRetrofit = new Retrofit
                    .Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getFirebaseTokenBaseURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), true))
                    .build();
        }
        return firebaseTokenRetrofit.create(NetworkServiceInterface.class);
    }

}
