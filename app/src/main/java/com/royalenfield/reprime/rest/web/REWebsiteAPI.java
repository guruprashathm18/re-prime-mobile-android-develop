package com.royalenfield.reprime.rest.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.rest.webservice.NetworkServiceInterface;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author BOP1KOR on 12/7/2018.
 * <p>
 * This class represents the RE WEBSITE API service.
 */

public class REWebsiteAPI {

    private Retrofit retrofit = null;
    private Retrofit retrofitJSON = null;
    private Retrofit retrofitNav = null;
    private Retrofit loggerRetrofit = null;
	private Retrofit connectedLoggerRetrofit = null;
    private Retrofit retrofitDIY = null;
    private Retrofit configuratorRetrofit=null;
    private Retrofit retrofitLocal = null;
    private Retrofit v3retrofit = null;
	private Retrofit v4retrofit = null;

    /**
     * This method creates a new instance of the RE Website API interface.
     *
     * @return The API interface
     */
    public WebsiteNetworkServiceInterface getWebsiteAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getAppBaseURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), true))
                    .build();
        }

        return retrofit.create(WebsiteNetworkServiceInterface.class);
    }
	/**
	 * This method creates a new instance of the RE Website API interface.
	 *
	 * @return The API interface
	 */
	public WebsiteNetworkServiceInterface getV4API() {
		Gson gson = new GsonBuilder()
				.setLenient()
				.create();
		if (v4retrofit == null) {
			v4retrofit = new Retrofit.Builder()
					.baseUrl(REUtils.getMobileappbaseURLs().getAppBaseURLV4()==null?REApplication.getAppContext().getString(R.string.v4API):REUtils.getMobileappbaseURLs().getAppBaseURLV4())
					.addConverterFactory(GsonConverterFactory.create(gson))
					.client(RESSLClient.createClient(REApplication.getAppContext(), true))
					.build();
		}

		return v4retrofit.create(WebsiteNetworkServiceInterface.class);
	}

    public WebsiteNetworkServiceInterface getJsonAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofitJSON == null) {
            retrofitJSON = new Retrofit.Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getAppBaseURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), false))
                    .build();
        }
        return retrofitJSON.create(WebsiteNetworkServiceInterface.class);
    }

    public WebsiteNetworkServiceInterface getWebsiteNavAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofitNav == null) {
            retrofitNav = new Retrofit.Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getTbtURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), true))
                    .build();
        }
        return retrofitNav.create(WebsiteNetworkServiceInterface.class);
    }

    public WebsiteNetworkServiceInterface getWebsiteLoggerAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (loggerRetrofit == null) {
            loggerRetrofit = new Retrofit.Builder()
                    .baseUrl(REUtils.getLoggerappbaseURLs().getBaseURL() + "/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), false))
                    .build();
        }
        return loggerRetrofit.create(WebsiteNetworkServiceInterface.class);
    }

	public WebsiteNetworkServiceInterface getConnectedLoggerAPI() {
		Gson gson = new GsonBuilder()
				.setLenient()
				.create();
		if (connectedLoggerRetrofit == null) {
			connectedLoggerRetrofit = new Retrofit.Builder()
					.baseUrl(REUtils.getMobileappbaseURLs().getConnectedLogsUrl() )
					.addConverterFactory(GsonConverterFactory.create(gson))
					.client(RESSLClient.createClient(REApplication.getAppContext(), false))
					.build();
		}
		return connectedLoggerRetrofit.create(WebsiteNetworkServiceInterface.class);
	}

    public WebsiteNetworkServiceInterface getAzureFileAPI() {
        if (retrofitDIY == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofitDIY = new Retrofit.Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getAppBaseURL())
                    .client(client)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofitDIY.create(WebsiteNetworkServiceInterface.class);
    }

    /**
     * This method creates a new instance of the RE TBT API interface.
     *
     * @return The API interface
     */
    public WebsiteNetworkServiceInterface getConfiguratorApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (configuratorRetrofit == null) {
            configuratorRetrofit = new Retrofit
                    .Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getConfiguratorServicesURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), true))
                    .build();
        }
        return configuratorRetrofit.create(WebsiteNetworkServiceInterface.class);
    }
    public WebsiteNetworkServiceInterface getLocalAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofitLocal == null) {
            retrofitLocal = new Retrofit.Builder()
                    .baseUrl(REConstants.ConnectedBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(),true))
                    .build();
        }
        return retrofitLocal.create(WebsiteNetworkServiceInterface.class);
    }

    public WebsiteNetworkServiceInterface getV3Api() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (v3retrofit == null) {
            v3retrofit = new Retrofit.Builder()
                    .baseUrl(REUtils.getMobileappbaseURLs().getFirebaseTokenBaseURL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(RESSLClient.createClient(REApplication.getAppContext(), true))
                    .build();
        }

        return v3retrofit.create(WebsiteNetworkServiceInterface.class);
    }

}
