package com.royalenfield.extensions

import android.os.Build
import android.os.StrictMode
import android.util.Log
import com.bosch.softtec.components.core.conversion.LengthUnit
import com.bosch.softtec.components.core.extensions.roundToPrecision
import com.bosch.softtec.components.core.models.Distance
import com.bosch.softtec.components.core.models.LatLng
import com.bosch.softtec.micro.tenzing.client.model.GpsPoint
import com.bosch.softtec.micro.tenzing.client.model.InlineResponse200
import com.bosch.softtec.micro.tenzing.client.model.Trip
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.utils.REUtils
import timber.log.Timber
import com.royalenfield.reprime.utils.RELog
import java.util.concurrent.TimeUnit
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Uploads the given BreadcrumbTrail and Bitmap asynchronously and showing a progressbar while doing so.
 */

private var time = "00:00:00"
private var distance = "0m"
const val LIMIT_FETCH_ALL = 10
/*
fun uploadTripBodyAndImage(
        tripBody: TripBody,
        tripImage: Bitmap?
) {
    @WorkerThread
    fun uploadTripBody(tripBody: TripBody) = REApplication.getInstance().cloudManager.uploadTrip(tripBody)

    @WorkerThread
    fun uploadTripImage(
            tripId: String,
            selectedBitmap: Bitmap
    ): String? {
        return REApplication.getInstance().cloudManager.uploadTripImage(tripId, selectedBitmap)
    }

    thread {
        try {
            //showProgressBar()

            val tripId = uploadTripBody(tripBody)
            if (tripId != null) {
                tripImage?.let { uploadTripImage(tripId, it) }


//                runOnUiThread {
//                    showSnackBar(R.string.upload_success)
//                    mapFragment.removeBreadcrumbTrail()
//                }
            }
        } finally {
            //hideProgressBar()
        }
    }
}*/

fun fetchAllCloudTrips(strTripId: String): InlineResponse200 {
   // Log.e("test", "fetched tripId = " + strTripId)
    enableThreadPolicy()
    val fetchedTripsResponse =
        if (strTripId.length == 0) {
            REApplication.getInstance().cloudManager.fetchAllTrips(LIMIT_FETCH_ALL)
        } else {
            REApplication.getInstance().cloudManager.fetchAllTrips(LIMIT_FETCH_ALL + 1, strTripId)
        }
   // Log.e("test", "fetched fetchedTripsResponse = " + fetchedTripsResponse)
    return fetchedTripsResponse
}

fun enableThreadPolicy() {
    val SDK_INT = Build.VERSION.SDK_INT
    if (SDK_INT > 8) {
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }
}

/*
fun drawRouteTrace(
        routeTrace: RouteTrace,
        routeTraceOptions: RouteTraceOptions,
        routeProvider : RouteProvider
)
{
    val routeCapabilities = routeProvider.capabilities
    val isSupportingVisualCongestion = routeCapabilities.isVisualCongestionSupported()

    val routeCongestionIsNonNull = routeTrace.edgeInfos.all { it.congestionType != null }
    val routeHasAtLeastOneKnownCongestion =
            routeTrace.edgeInfos.any { it.congestionType != null && it.congestionType != CongestionType.UNKNOWN }
    val routeHasLiveTraffic = routeCongestionIsNonNull && routeHasAtLeastOneKnownCongestion

    val isAlternative = routeTraceOptions.isAlternative
    val polylineOptionsList = if (isSupportingVisualCongestion && !isAlternative && routeHasLiveTraffic)
    {
        polylineOptionBuilder.buildPolylineOptionsWithLiveTraffic(routeTrace, routeTraceOptions)
    }
    else
    {
        polylineOptionBuilder.buildPolylineOptionsForLiveTraffic(routeTrace, routeTraceOptions)
    }

    polylineOptionsList.forEach {
        val polyline = map.addPolyline(it)
        addedPolylines[polyline] = routeTraceOptions.onClickRouteListener
    }
}*/

/**
 * For adding the StartLocation Marker on the map
 */
fun showMarker(startLocation: LatLng, googleMap: GoogleMap) {
    val startLocationMarker: Marker
    if (googleMap != null) startLocationMarker =
        googleMap.addMarker(
            MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(
                    com.google.android.gms.maps.model.LatLng(
                        startLocation.latitude,
                        startLocation.longitude
                    )
                ).draggable(false)
        )!!
}

fun calcTrackDistance(distance: Distance): String {

    val formattedDistance =
        if (distance.distanceValue < 1000) {
            distance.lengthUnit.toMeters(distance.distanceValue).roundToPrecision(0).toInt()
                .toString() + " m"
        } else {
            distance.lengthUnit.toKilometers(distance.distanceValue).roundToPrecision(3)
                .toString() + " km"
        }
    return formattedDistance;
}

fun calcTrackTime(trip: Trip): String {
    val points = if (trip != null) trip!!.track
    else return ""
    var start = points.last().time
    var end = start
    points.forEach {
        if (it.time < start) {
            start = it.time
        }
        if (it.time > end) {
            end = it.time
        }
    }
    var time2 = end.toInstant().toEpochMilli() - start.toInstant().toEpochMilli()
    if (time2 >= 360000000) {
        time = "99:59:59"
    } else {
        time2 /= 1000
        var timeMinutes = time2 % 3600
        time2 /= 3600
        val timeSeconds = timeMinutes % 60
        timeMinutes /= 60
        var hourEdit = ""
        if (time2 < 10) hourEdit = "0"
        var minuteEdit = ""
        if (timeMinutes < 10) minuteEdit = "0"
        var secondEdit = ""
        if (timeSeconds < 10) secondEdit = "0"
        time = hourEdit + time.toString() + ":" + minuteEdit + timeMinutes.toString() +
                ":" + secondEdit + timeSeconds.toString()
    }
    return time

}

fun calcTrackTime(duration: Long): String {
    var etaDisplay = ""
    val time = TimeUnit.MILLISECONDS.toHours(duration)
    var separator = "."
    if (REUtils.isPreferenceComma()) {
        separator = ","
    }
    if (time!! > 1) {
        etaDisplay =
            time.toString() + separator + TimeUnit.MILLISECONDS.toMinutes(duration).toString() + " hr"
        RELog.e("hrs is :" + etaDisplay);
    } else {
        etaDisplay = TimeUnit.MILLISECONDS.toMinutes(duration)
            .toString() +  " min"
        RELog.e("minute is :" + etaDisplay);
    }
    return etaDisplay
}


fun calcTrackDistance(track: List<GpsPoint>): String {
    var totalDistance = 0.0
    if (track.size <= 1) return ""
    for (i in 1 until track.size) {
        val lat1 = track[i - 1].latitude
        val lat2 = track[i].latitude
        val lng1 = track[i - 1].longitude
        val lng2 = track[i].longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)

        totalDistance += 2 * asin(
            sqrt(
                sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) *
                        cos(Math.toRadians(lat2)) * sin(dLng / 2) * sin(dLng / 2)
            )
        ) * 6366000
    }

    val distance2 = Distance(totalDistance, LengthUnit.METERS)

    if (distance2.distanceValue < 1000) {
        distance =
            distance2.lengthUnit.toMeters(distance2.distanceValue).roundToPrecision(0).toInt()
                .toString() + " m"
    } else {
        distance = distance2.lengthUnit.toKilometers(distance2.distanceValue).roundToPrecision(3)
            .toString() + " km"
    }
    //viewModel.trackDistance = this.distance
    return distance
}
