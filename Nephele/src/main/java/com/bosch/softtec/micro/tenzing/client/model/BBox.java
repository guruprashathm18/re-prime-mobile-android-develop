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

import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Holds the coordinates for a geo bounding box.
 */
@ApiModel(description = "Holds the coordinates for a geo bounding box.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-02-06T13:30:17.808+01:00[Europe/Berlin]")
public class BBox implements OneOfBBoxCircle {
  public static final String SERIALIZED_NAME_SOUTH = "south";
  @SerializedName(SERIALIZED_NAME_SOUTH)
  private Double south;

  public static final String SERIALIZED_NAME_NORTH = "north";
  @SerializedName(SERIALIZED_NAME_NORTH)
  private Double north;

  public static final String SERIALIZED_NAME_WEST = "west";
  @SerializedName(SERIALIZED_NAME_WEST)
  private Double west;

  public static final String SERIALIZED_NAME_EAST = "east";
  @SerializedName(SERIALIZED_NAME_EAST)
  private Double east;


  public BBox south(Double south) {

    this.south = south;
    return this;
  }

   /**
   * The latitude, in decimal degrees.
   * minimum: -90
   * maximum: 90
   * @return south
  **/
  @ApiModelProperty(example = "52.150902", required = true, value = "The latitude, in decimal degrees.")

  public Double getSouth() {
    return south;
  }


  public void setSouth(Double south) {
    this.south = south;
  }


  public BBox north(Double north) {

    this.north = north;
    return this;
  }

   /**
   * The latitude, in decimal degrees.
   * minimum: -90
   * maximum: 90
   * @return north
  **/
  @ApiModelProperty(example = "52.150902", required = true, value = "The latitude, in decimal degrees.")

  public Double getNorth() {
    return north;
  }


  public void setNorth(Double north) {
    this.north = north;
  }


  public BBox west(Double west) {

    this.west = west;
    return this;
  }

   /**
   * The longitude, in decimal degrees.
   * minimum: -180
   * maximum: 180
   * @return west
  **/
  @ApiModelProperty(example = "9.951", required = true, value = "The longitude, in decimal degrees.")

  public Double getWest() {
    return west;
  }


  public void setWest(Double west) {
    this.west = west;
  }


  public BBox east(Double east) {

    this.east = east;
    return this;
  }

   /**
   * The longitude, in decimal degrees.
   * minimum: -180
   * maximum: 180
   * @return east
  **/
  @ApiModelProperty(example = "9.951", required = true, value = "The longitude, in decimal degrees.")

  public Double getEast() {
    return east;
  }


  public void setEast(Double east) {
    this.east = east;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BBox bbox = (BBox) o;
    return Objects.equals(this.south, bbox.south) &&
        Objects.equals(this.north, bbox.north) &&
        Objects.equals(this.west, bbox.west) &&
        Objects.equals(this.east, bbox.east);
  }

  @Override
  public int hashCode() {
    return Objects.hash(south, north, west, east);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BBox {\n");
    sb.append("    south: ").append(toIndentedString(south)).append("\n");
    sb.append("    north: ").append(toIndentedString(north)).append("\n");
    sb.append("    west: ").append(toIndentedString(west)).append("\n");
    sb.append("    east: ").append(toIndentedString(east)).append("\n");
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

