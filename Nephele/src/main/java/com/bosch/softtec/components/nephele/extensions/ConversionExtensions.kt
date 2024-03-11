/*
 * Copyright (c) 2020 Bosch SoftTec GmbH All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Bosch
 * SoftTec GmbH ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Bosch SoftTec (BSOT).
 *
 * BSOT MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. BSOT SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.bosch.softtec.components.nephele.extensions

import com.bosch.softtec.components.core.conversion.LengthUnit
import com.bosch.softtec.components.core.conversion.SpeedUnit
import com.bosch.softtec.components.core.models.Accuracy
import com.bosch.softtec.components.core.models.Distance
import com.bosch.softtec.components.core.models.Location
import com.bosch.softtec.components.core.models.Speed
import com.bosch.softtec.components.core.models.breadcrumb.Breadcrumb
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail
import com.bosch.softtec.components.core.models.breadcrumb.RideSummary
import com.bosch.softtec.components.core.models.breadcrumb.SourceType
import com.bosch.softtec.micro.tenzing.client.model.GpsPoint
import com.bosch.softtec.micro.tenzing.client.model.Trip
import com.bosch.softtec.micro.tenzing.client.model.TripBodyWithImageUrl
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

/**
 * Converts the BreadcrumbTrail to a TripBody which can be used with CloudManager#uploadTrip.
 */
fun BreadcrumbTrail.toTripBodyWithImageUrl(): TripBodyWithImageUrl {
    val track = breadcrumbs.map { it.toGpsPoint() }
    val directions = breadcrumbs.filter { it.isNavigationPoint }.map { it.toGpsPoint() }

    return TripBodyWithImageUrl()
        .track(track)
        .directions(directions)
}

/**
 * Converts the breadcrumb to [@see GpsPoint] for use in CloudApi models.
 */
private fun Breadcrumb.toGpsPoint(): GpsPoint {
    val gpsPoint = GpsPoint()
        .latitude(location.latitude)
        .longitude(location.longitude)

    // Populate optional values
    location.altitude?.also { gpsPoint.altitude(it.toMeters()) }
    location.accuracy?.also { gpsPoint.accuracy(it.toMeters().toFloat()) }
    location.bearing?.also { gpsPoint.bearing(it) }
    location.speed?.also { gpsPoint.speed(it.toMetersPerSecond().toFloat()) }
    utcTime?.also { gpsPoint.time(it) }

    return gpsPoint
}


/**
 * Returns the contained timestamp as OffsetDateTime object in the UTC timezone.
 */
private val Breadcrumb.utcTime: OffsetDateTime?
    get() {
        val instant = Instant.ofEpochMilli(location.timestamp)
        val zoneId = ZoneId.of("UTC")

        return OffsetDateTime.ofInstant(instant, zoneId)
    }


/**
 * Converts the Trip to a BreadcrumbTrail.
 */
fun Trip.toBreadcrumbTrail(): BreadcrumbTrail {
    val breadcrumbs = track.map { it.toBreadcrumb(directions) }

    return BreadcrumbTrail(breadcrumbs)
        .apply {
            this.isPaused = false
            this.sourceType = SourceType.IMPORTED
            this.rideSummary = RideSummary.createInstance(breadcrumbs, null)
        }
}

/**
 * Converts a GpsPoint into a Breadcrumb. Since a GpsPoint does not contain any vehicle data the
 * returned Breadcrumbs vehicle data will be null.
 */
private fun GpsPoint.toBreadcrumb(directions: List<GpsPoint>): Breadcrumb {
    val instant = time.toInstant()
    val timestamp = instant.toEpochMilli()

    val distance = altitude?.let { Distance(it, LengthUnit.METERS) }
    val speed = speed?.let { Speed(it, SpeedUnit.METERS_PER_SECOND) }
    val accuracy = accuracy?.let { Accuracy(it, LengthUnit.METERS) }

    val location = Location(
        latitude = latitude,
        longitude = longitude,
        altitude = distance,
        speed = speed,
        accuracy = accuracy,
        bearing = bearing,
        timestamp = timestamp
    )

    val vehicleData = null // does not contain vehicle data yet
    val isNavigationPoint = directions.contains(this)

    return Breadcrumb(location, vehicleData, isNavigationPoint)
}
