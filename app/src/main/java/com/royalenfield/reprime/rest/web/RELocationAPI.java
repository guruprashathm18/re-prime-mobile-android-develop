package com.royalenfield.reprime.rest.web;

import com.royalenfield.reprime.models.response.web.navigation.GetLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author BOP1KOR on 12/7/2018.
 * <p>
 * This class represents the RE WEBSITE API service.
 */

public interface RELocationAPI {

    String BASE_URL = "https://maps.googleapis.com/";

    @GET("maps/api/geocode/json")
    Call<GetLocation> getLocationFromLatLng(@Query("latlng") String latlng, @Query("key") String key);
}
