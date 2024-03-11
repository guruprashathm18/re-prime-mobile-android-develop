package com.royalenfield.reprime.rest.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.rest.web.RESSLClient;
import com.royalenfield.reprime.utils.REUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RENotificationAPI {

    private Retrofit retrofit = null;
    private Retrofit retrofitPayment = null;

    /**
     * This method creates a new instance of the RE Notification API interface.
     *
     * @return The API interface
     */
    public NetworkServiceInterface getNotificationApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getDeviceTokenServicesURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(),true))
                    .build();
        }
        return retrofit.create(NetworkServiceInterface.class);
    }

    public NetworkServiceInterface getPaymentApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofitPayment == null) {
            retrofitPayment = new Retrofit
                    .Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getGeneratepaymentmessageurl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(),true))
                    .build();
        }
        return retrofitPayment.create(NetworkServiceInterface.class);
    }
}
