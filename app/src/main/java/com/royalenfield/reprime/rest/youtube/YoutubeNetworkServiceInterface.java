package com.royalenfield.reprime.rest.youtube;

import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeNetworkServiceInterface {

    //  @GET("id=7lCDEYXw3mM&key=YOUR_API_KEY&fields=items(id,snippet(channelId,title,categoryId),statistics)&part=snippet,statistics")
    @GET("v3/videos")
    Call<VideoAttribute> getVideoAttributes(@Query("id") String id, @Query("key") String apiKey, @Query("fields") String fields, @Query("part") String part);


}
