/*
 * Tenzing Microservice
 * Tenzing is a Bosch SoftTec microservice for storing, managing and distributing user trips.
 *
 * The version of the OpenAPI document: 1.0.0
 * Contact: BSOT_PJ-SC3_Cloud-CF@de.bosch.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.bosch.softtec.micro.tenzing.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * InlineResponse200
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-02-06T13:30:17.808+01:00[Europe/Berlin]")
public class InlineResponse200 {
  public static final String SERIALIZED_NAME_TRIPS = "trips";
  @SerializedName(SERIALIZED_NAME_TRIPS)
  private List<TripMetadata> trips = new ArrayList<TripMetadata>();

  public static final String SERIALIZED_NAME_NEXT = "next";
  @SerializedName(SERIALIZED_NAME_NEXT)
  private String next;


  public InlineResponse200 trips(List<TripMetadata> trips) {

    this.trips = trips;
    return this;
  }

  public InlineResponse200 addTripsItem(TripMetadata tripsItem) {
    this.trips.add(tripsItem);
    return this;
  }

   /**
   * Get trips
   * @return trips
  **/
  @ApiModelProperty(required = true, value = "")

  public List<TripMetadata> getTrips() {
    return trips;
  }


  public void setTrips(List<TripMetadata> trips) {
    this.trips = trips;
  }


  public InlineResponse200 next(String next) {

    this.next = next;
    return this;
  }

   /**
   * The cursor to get the next result set which the next call of this endpoint.
   * @return next
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "5da983bef8e66a36e1f4ce2c", value = "The cursor to get the next result set which the next call of this endpoint.")

  public String getNext() {
    return next;
  }


  public void setNext(String next) {
    this.next = next;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse200 inlineResponse200 = (InlineResponse200) o;
    return Objects.equals(this.trips, inlineResponse200.trips) &&
        Objects.equals(this.next, inlineResponse200.next);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trips, next);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse200 {\n");
    sb.append("    trips: ").append(toIndentedString(trips)).append("\n");
    sb.append("    next: ").append(toIndentedString(next)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

