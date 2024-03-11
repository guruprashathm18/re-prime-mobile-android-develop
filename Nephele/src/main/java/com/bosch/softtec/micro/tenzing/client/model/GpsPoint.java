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

import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Holds GPS coordinates of a specific geographical point and all additional information related to it.
 */
@ApiModel(description = "Holds GPS coordinates of a specific geographical point and all additional information related to it.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-02-06T13:30:17.808+01:00[Europe/Berlin]")
public class GpsPoint {
  public static final String SERIALIZED_NAME_LATITUDE = "latitude";
  @SerializedName(SERIALIZED_NAME_LATITUDE)
  private Double latitude;

  public static final String SERIALIZED_NAME_LONGITUDE = "longitude";
  @SerializedName(SERIALIZED_NAME_LONGITUDE)
  private Double longitude;

  public static final String SERIALIZED_NAME_ALTITUDE = "altitude";
  @SerializedName(SERIALIZED_NAME_ALTITUDE)
  private Double altitude;

  public static final String SERIALIZED_NAME_BEARING = "bearing";
  @SerializedName(SERIALIZED_NAME_BEARING)
  private Float bearing;

  public static final String SERIALIZED_NAME_SPEED = "speed";
  @SerializedName(SERIALIZED_NAME_SPEED)
  private Float speed;

  public static final String SERIALIZED_NAME_POI_PROPERTIES = "poiProperties";
  @SerializedName(SERIALIZED_NAME_POI_PROPERTIES)
  private PoiProperties poiProperties;

  public static final String SERIALIZED_NAME_ACCURACY = "accuracy";
  @SerializedName(SERIALIZED_NAME_ACCURACY)
  private Float accuracy;

  public static final String SERIALIZED_NAME_TIME = "time";
  @SerializedName(SERIALIZED_NAME_TIME)
  private OffsetDateTime time;


  public GpsPoint latitude(Double latitude) {

    this.latitude = latitude;
    return this;
  }

   /**
   * The latitude, in decimal degrees.
   * minimum: -90
   * maximum: 90
   * @return latitude
  **/
  @ApiModelProperty(example = "52.150902", required = true, value = "The latitude, in decimal degrees.")

  public Double getLatitude() {
    return latitude;
  }


  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }


  public GpsPoint longitude(Double longitude) {

    this.longitude = longitude;
    return this;
  }

   /**
   * The longitude, in decimal degrees.
   * minimum: -180
   * maximum: 180
   * @return longitude
  **/
  @ApiModelProperty(example = "9.951", required = true, value = "The longitude, in decimal degrees.")

  public Double getLongitude() {
    return longitude;
  }


  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }


  public GpsPoint altitude(Double altitude) {

    this.altitude = altitude;
    return this;
  }

   /**
   * The altitude, in meters above the WGS 84 reference ellipsoid.
   * minimum: -500
   * maximum: 10000
   * @return altitude
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "-3.123", value = "The altitude, in meters above the WGS 84 reference ellipsoid.")

  public Double getAltitude() {
    return altitude;
  }


  public void setAltitude(Double altitude) {
    this.altitude = altitude;
  }


  public GpsPoint bearing(Float bearing) {

    this.bearing = bearing;
    return this;
  }

   /**
   * The bearing, in degrees.   Bearing is the horizontal direction of travel of the client device, and is not related to the device  orientation. It is guaranteed to be in the range (0.0, 360.0] if the device has a bearing.
   * minimum: 0
   * maximum: 360
   * @return bearing
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "122.05", value = "The bearing, in degrees.   Bearing is the horizontal direction of travel of the client device, and is not related to the device  orientation. It is guaranteed to be in the range (0.0, 360.0] if the device has a bearing.")

  public Float getBearing() {
    return bearing;
  }


  public void setBearing(Float bearing) {
    this.bearing = bearing;
  }


  public GpsPoint speed(Float speed) {

    this.speed = speed;
    return this;
  }

   /**
   * The speed, in meters/second over ground.
   * minimum: 0
   * @return speed
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "15.2", value = "The speed, in meters/second over ground.")

  public Float getSpeed() {
    return speed;
  }


  public void setSpeed(Float speed) {
    this.speed = speed;
  }


  public GpsPoint poiProperties(PoiProperties poiProperties) {

    this.poiProperties = poiProperties;
    return this;
  }

   /**
   * Get poiProperties
   * @return poiProperties
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public PoiProperties getPoiProperties() {
    return poiProperties;
  }


  public void setPoiProperties(PoiProperties poiProperties) {
    this.poiProperties = poiProperties;
  }


  public GpsPoint accuracy(Float accuracy) {

    this.accuracy = accuracy;
    return this;
  }

   /**
   * The estimated horizontal accuracy of this point, radial, in meters.   We define horizontal accuracy as the radius of 68% confidence. In other words, if you draw a circle centered  at this point&#39;s latitude and longitude, and with a radius equal to the accuracy, then there is a 68%  probability that the true point is inside the circle.   This accuracy estimation is only concerned with horizontal accuracy, and does not indicate the accuracy of  bearing, velocity or altitude if those are included in this point.  horizontal accuracy, then 0.0 is returned.
   * minimum: 0
   * @return accuracy
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "5.56", value = "The estimated horizontal accuracy of this point, radial, in meters.   We define horizontal accuracy as the radius of 68% confidence. In other words, if you draw a circle centered  at this point's latitude and longitude, and with a radius equal to the accuracy, then there is a 68%  probability that the true point is inside the circle.   This accuracy estimation is only concerned with horizontal accuracy, and does not indicate the accuracy of  bearing, velocity or altitude if those are included in this point.  horizontal accuracy, then 0.0 is returned.")

  public Float getAccuracy() {
    return accuracy;
  }


  public void setAccuracy(Float accuracy) {
    this.accuracy = accuracy;
  }


  public GpsPoint time(OffsetDateTime time) {

    this.time = time;
    return this;
  }

   /**
   * The UTC time of this point, in milliseconds since January 1, 1970.
   * @return time
  **/
  @ApiModelProperty(example = "2019-05-15T11:17:21Z", required = true, value = "The UTC time of this point, in milliseconds since January 1, 1970.")

  public OffsetDateTime getTime() {
    return time;
  }


  public void setTime(OffsetDateTime time) {
    this.time = time;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GpsPoint gpsPoint = (GpsPoint) o;
    return Objects.equals(this.latitude, gpsPoint.latitude) &&
        Objects.equals(this.longitude, gpsPoint.longitude) &&
        Objects.equals(this.altitude, gpsPoint.altitude) &&
        Objects.equals(this.bearing, gpsPoint.bearing) &&
        Objects.equals(this.speed, gpsPoint.speed) &&
        Objects.equals(this.poiProperties, gpsPoint.poiProperties) &&
        Objects.equals(this.accuracy, gpsPoint.accuracy) &&
        Objects.equals(this.time, gpsPoint.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(latitude, longitude, altitude, bearing, speed, poiProperties, accuracy, time);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GpsPoint {\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    altitude: ").append(toIndentedString(altitude)).append("\n");
    sb.append("    bearing: ").append(toIndentedString(bearing)).append("\n");
    sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
    sb.append("    poiProperties: ").append(toIndentedString(poiProperties)).append("\n");
    sb.append("    accuracy: ").append(toIndentedString(accuracy)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
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
