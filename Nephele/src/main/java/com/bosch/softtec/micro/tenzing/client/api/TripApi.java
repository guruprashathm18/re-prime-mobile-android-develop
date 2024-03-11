package com.bosch.softtec.micro.tenzing.client.api;

import com.bosch.softtec.micro.tenzing.client.model.InlineResponse200;
import com.bosch.softtec.micro.tenzing.client.model.JsonPatch;
import com.bosch.softtec.micro.tenzing.client.model.SearchContext;
import com.bosch.softtec.micro.tenzing.client.model.Trip;
import com.bosch.softtec.micro.tenzing.client.model.TripBodyWithImageUrl;
import com.bosch.softtec.micro.tenzing.client.model.VisibilityType;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TripApi {
  /**
   * Delete trip by ID
   * Deletes a trip by a given ID and all resources related to that trip (e.g. all images).
   * @param tripId The id of the trip. (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("trips/{tripId}")
  Call<Void> tripDelete(
          @Path("tripId") String tripId
  );

  /**
   * Find trip by ID
   * Returns all data of a specific trip.
   * @param tripId The id of the trip. (required)
   * @return Call&lt;Trip&gt;
   */
  @GET("trips/{tripId}")
  Call<Trip> tripGet(
          @Path("tripId") String tripId
  );

  /**
   * Update a trip with JSON Patch by ID
   * Updates an existing trip with the [JSON patch](https://tools.ietf.org/html/rfc6902) methods. Currently only the &#x60;add&#x60;, &#x60;remove&#x60; and &#x60;replace&#x60; operation is supported by the PATCH endpoint!
   * @param tripId The id of the trip. (required)
   * @param jsonPatch Represents a request object to update an existing trip. (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json-patch+json"
  })
  @PATCH("trips/{tripId}")
  Call<Void> tripPatch(
          @Path("tripId") String tripId, @Body List<JsonPatch> jsonPatch
  );

  /**
   * Add a new trip
   * Creates a new trip with the given data.
   * @param tripBodyWithImage Represents a request object that adds a new trip. (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("trips/")
  Call<Void> tripPost(
          @Body TripBodyWithImageUrl tripBodyWithImage
  );

  /**
   * Get all trips
   * Gets all &#x60;PRIVATE&#x60; and/or &#x60;SHARED&#x60; trips for the requested user. &#x60;PUBLIC&#x60; trips are not returned, corresponding filters are ignored without errors.
   * @param limit Limits the number of trips in the result. (optional, default to 10d)
   * @param next  (optional)
   * @param filterVis Filters the result by the given trip visibility type. Multiple types can be specified separated by a comma. (optional, default to new ArrayList&lt;VisibilityType&gt;())
   * @return Call&lt;InlineResponse200&gt;
   */
  @GET("trips/")
  Call<InlineResponse200> tripsGet(
          @Query("limit") BigDecimal limit, @Query("next") String next, @Query("filter.vis") List<VisibilityType> filterVis
  );

  /**
   * Search for trips
   * Searches for trips based on the given search context.
   * @param searchContext Represents a request object to specify parameters for searching trips. (required)
   * @param limit Limits the number of trips in the result. (optional, default to 10d)
   * @param next  (optional)
   * @param filterVis Filters the result by the given trip visibility type. Multiple types can be specified separated by a comma. (optional, default to new ArrayList&lt;VisibilityType&gt;())
   * @return Call&lt;InlineResponse200&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("trips/searches")
  Call<InlineResponse200> tripsSearch(
          @Body SearchContext searchContext, @Query("limit") BigDecimal limit, @Query("next") String next, @Query("filter.vis") List<VisibilityType> filterVis
  );

}
