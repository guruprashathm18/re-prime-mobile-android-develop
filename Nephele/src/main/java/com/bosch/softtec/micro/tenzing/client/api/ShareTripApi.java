package com.bosch.softtec.micro.tenzing.client.api;

import com.bosch.softtec.micro.tenzing.client.model.ShareTicket;
import com.bosch.softtec.micro.tenzing.client.model.ShareTicketBody;
import com.bosch.softtec.micro.tenzing.client.model.ShareTicketCode;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ShareTripApi {
  /**
   * Redeem shared trip
   * Redeem shared ticket based on the trip ID and defined expiration.
   * @param shareTicketCode Represents a request object to specify parameters to redeem a shared trips. (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("sharing/redeem")
  Call<Void> redeemSharedTrip(
          @Body ShareTicketCode shareTicketCode
  );

  /**
   * Share trip
   * Share trip based on the trip ID and defined expiration.
   * @param shareTicketBody Represents a request object to specify parameters for sharing trips. (required)
   * @return Call&lt;ShareTicket&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("sharing/getOrCreate")
  Call<ShareTicket> shareTrip(
          @Body ShareTicketBody shareTicketBody
  );

}
