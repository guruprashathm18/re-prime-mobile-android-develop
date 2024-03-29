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

import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * TripAllOf
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-02-06T13:30:17.808+01:00[Europe/Berlin]")
public class TripAllOf {
  public static final String SERIALIZED_NAME_TRIP_ID = "tripId";
  @SerializedName(SERIALIZED_NAME_TRIP_ID)
  private String tripId;

  public static final String SERIALIZED_NAME_OWNER = "owner";
  @SerializedName(SERIALIZED_NAME_OWNER)
  private String owner;

  public static final String SERIALIZED_NAME_CREATED = "created";
  @SerializedName(SERIALIZED_NAME_CREATED)
  private OffsetDateTime created;

  public static final String SERIALIZED_NAME_MODIFIED = "modified";
  @SerializedName(SERIALIZED_NAME_MODIFIED)
  private OffsetDateTime modified;

  public static final String SERIALIZED_NAME_LINKS = "links";
  @SerializedName(SERIALIZED_NAME_LINKS)
  private List<Link> links = null;


  public TripAllOf tripId(String tripId) {

    this.tripId = tripId;
    return this;
  }

   /**
   * The unique identifier of an entity.
   * @return tripId
  **/
  @ApiModelProperty(example = "5da983bef8e66a36e1f4ce2c", required = true, value = "The unique identifier of an entity.")

  public String getTripId() {
    return tripId;
  }


  public void setTripId(String tripId) {
    this.tripId = tripId;
  }


  public TripAllOf owner(String owner) {

    this.owner = owner;
    return this;
  }

   /**
   * The id of the user that is the owner/author of this trip.
   * @return owner
  **/
  @ApiModelProperty(example = "tenzing", required = true, value = "The id of the user that is the owner/author of this trip.")

  public String getOwner() {
    return owner;
  }


  public void setOwner(String owner) {
    this.owner = owner;
  }


  public TripAllOf created(OffsetDateTime created) {

    this.created = created;
    return this;
  }

   /**
   * The point in UTC time this trip was created, in milliseconds since January 1, 1970.
   * @return created
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2019-05-15T11:17:21Z", value = "The point in UTC time this trip was created, in milliseconds since January 1, 1970.")

  public OffsetDateTime getCreated() {
    return created;
  }


  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }


  public TripAllOf modified(OffsetDateTime modified) {

    this.modified = modified;
    return this;
  }

   /**
   * The point in UTC time this trip was modified, in milliseconds since January 1, 1970.
   * @return modified
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2019-05-18T10:17:21Z", value = "The point in UTC time this trip was modified, in milliseconds since January 1, 1970.")

  public OffsetDateTime getModified() {
    return modified;
  }


  public void setModified(OffsetDateTime modified) {
    this.modified = modified;
  }


  public TripAllOf links(List<Link> links) {

    this.links = links;
    return this;
  }

  public TripAllOf addLinksItem(Link linksItem) {
    if (this.links == null) {
      this.links = new ArrayList<Link>();
    }
    this.links.add(linksItem);
    return this;
  }

   /**
   * Contains the links of all resources of this entity. If available, images are linked to the trip. At a minimum, however, there is a link to the trip entity itself.
   * @return links
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains the links of all resources of this entity. If available, images are linked to the trip. At a minimum, however, there is a link to the trip entity itself.")

  public List<Link> getLinks() {
    return links;
  }


  public void setLinks(List<Link> links) {
    this.links = links;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TripAllOf tripAllOf = (TripAllOf) o;
    return Objects.equals(this.tripId, tripAllOf.tripId) &&
        Objects.equals(this.owner, tripAllOf.owner) &&
        Objects.equals(this.created, tripAllOf.created) &&
        Objects.equals(this.modified, tripAllOf.modified) &&
        Objects.equals(this.links, tripAllOf.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tripId, owner, created, modified, links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TripAllOf {\n");
    sb.append("    tripId: ").append(toIndentedString(tripId)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

