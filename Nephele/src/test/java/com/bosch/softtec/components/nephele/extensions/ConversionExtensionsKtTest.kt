package com.bosch.softtec.components.nephele.extensions

import com.bosch.softtec.components.core.models.Location
import com.bosch.softtec.components.core.models.breadcrumb.Breadcrumb
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail
import com.bosch.softtec.components.core.models.breadcrumb.SourceType
import com.bosch.softtec.micro.tenzing.client.model.GpsPoint
import com.bosch.softtec.micro.tenzing.client.model.Link
import com.bosch.softtec.micro.tenzing.client.model.Trip
import com.bosch.softtec.micro.tenzing.client.model.VisibilityType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

/**
 * @author Andre Weber
 * @since 2019-11-06
 */
class ConversionExtensionsKtTest {

    @Test
    fun toTripBody() {
        val sourceType = SourceType.RECORDED
        val isPaused = true

        val breadcrumbs = listOf(
            Breadcrumb(Location(10.0, 20.0), null, true),
            Breadcrumb(Location(30.0, 40.0), null, false),
            Breadcrumb(Location(50.0, 60.0), null, false),
            Breadcrumb(Location(70.0, 80.0), null, false),
            Breadcrumb(Location(90.0, 100.0), null, true)
        )

        val breadcrumbTrail = BreadcrumbTrail(
            breadcrumbs = breadcrumbs
        ).apply {
            this.isPaused = isPaused
            this.sourceType = sourceType
        }

        val tripBody = breadcrumbTrail.toTripBodyWithImageUrl()

        assertNull(tripBody.name)
        assertNull(tripBody.description)
        assertEquals(5, tripBody.track.size)
        assertEquals(2, tripBody.directions.size)
        assertNull(tripBody.visibility)
    }

    @Test
    fun toBreadcrumbTrail() {
        val trip = createTrip()
        val breadcrumbTrail = trip.toBreadcrumbTrail()
        val track = trip.track

        assertEquals(SourceType.IMPORTED, breadcrumbTrail.sourceType)
        assertEquals(false, breadcrumbTrail.isPaused)
        assertEquals(track.size, breadcrumbTrail.breadcrumbs.size)
    }

    private fun createTrip(): Trip {
        val tripId = "someTripId"
        val name = "someName"
        val description = "someDescription"
        val owner = "someOwner"
        val tracks = createTracks()
        val directions = createDirections(tracks)
        val links = createLinks()
        val visibilityType = VisibilityType.SHARED
        val created = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1000L), ZoneId.of("UTC"))
        val modified = OffsetDateTime.ofInstant(Instant.ofEpochMilli(2000L), ZoneId.of("UTC"))

        return Trip()
            .tripId(tripId)
            .name(name)
            .description(description)
            .owner(owner)
            .track(tracks)
            .directions(directions)
            .links(links)
            .visibility(visibilityType)
            .created(created)
            .modified(modified)
    }

    private fun createTracks(): List<GpsPoint> {
        val zoneId = ZoneId.of("UTC")

        val gpsPoint1 = GpsPoint().latitude(10.0).longitude(20.0)
            .time(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1000L), zoneId))
        val gpsPoint2 = GpsPoint().latitude(20.0).longitude(30.0)
            .time(OffsetDateTime.ofInstant(Instant.ofEpochMilli(2000L), zoneId))
        val gpsPoint3 = GpsPoint().latitude(30.0).longitude(40.0)
            .time(OffsetDateTime.ofInstant(Instant.ofEpochMilli(3000L), zoneId))
        val gpsPoint4 = GpsPoint().latitude(40.0).longitude(50.0)
            .time(OffsetDateTime.ofInstant(Instant.ofEpochMilli(4000L), zoneId))
        val gpsPoint5 = GpsPoint().latitude(50.0).longitude(60.0)
            .time(OffsetDateTime.ofInstant(Instant.ofEpochMilli(5000L), zoneId))

        return listOf<GpsPoint>(gpsPoint1, gpsPoint2, gpsPoint3, gpsPoint4, gpsPoint5)
    }

    private fun createDirections(track: List<GpsPoint>): List<GpsPoint> {
        return listOf(track.first(), track.last())
    }

    private fun createLinks(): List<Link> {
        val link1 = Link()
            .rel("self")
            .href("/api/v1/trips/19a61c14-2fda-436f-935d-be4a21f7e182")

        val link2 = Link()
            .rel("self")
            .href("/api/v1/trips/19a61c14-2fda-436f-935d-be4a21f7e183")

        val link3 = Link()
            .rel("self")
            .href("/api/v1/trips/19a61c14-2fda-436f-935d-be4a21f7e184")

        return listOf(link1, link2, link3)
    }
}
