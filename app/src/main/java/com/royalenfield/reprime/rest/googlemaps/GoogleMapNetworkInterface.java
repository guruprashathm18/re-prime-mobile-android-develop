package com.royalenfield.reprime.rest.googlemaps;

import com.royalenfield.reprime.models.response.googlemap.directions.DirectionsResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.AutoCompleteSearchResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.NearBySearchResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.POIDetailsResponse;
import com.royalenfield.reprime.models.response.web.navigation.GetLocation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapNetworkInterface {

    @GET("place/nearbysearch/json")
    Call<NearBySearchResponse> getPOI(@Query("key") String key, @Query("location") String location,
                                      @Query("rankby") String distance);

    @GET("place/textsearch/json")
    Call<NearBySearchResponse> getPlaces(@Query("key") String key, @Query("query") String query,
                                         @Query("region") String region);

    @GET("directions/json")
    Call<DirectionsResponse> getDirections(@Query("key") String key, @Query("origin") String origin,
                                           @Query("destination") String destination, @Query("region") String region,
                                           @Query("mode") String mode, @Query("waypoints") String waypoints,
                                           @Query("units") String units);

    @GET("place/autocomplete/json")
    Call<AutoCompleteSearchResponse> getPlaceId(@Query("key") String key, @Query("input") String input,
                                                @Query("origin") String origin, @Query("location") String location,
                                                @Query("radius") String radius);

    @GET("place/details/json")
    Call<POIDetailsResponse> getPlaceDetails(@Query("key") String key, @Query("place_id") String placeId,
                                             @Query("fields") String fields);

    @GET("geocode/json")
    Call<GetLocation> getPlaceName(@Query("key") String key, @Query("latlng") String latlng);

}
