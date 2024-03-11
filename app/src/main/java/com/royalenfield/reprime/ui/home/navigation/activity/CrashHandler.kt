package com.royalenfield.reprime.ui.home.navigation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bosch.softtec.components.core.models.LatLng
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail
import com.bosch.softtec.components.midgard.core.directions.models.WayPoint
import com.bosch.softtec.components.theseus.BreadcrumbTrailManager
import com.royalenfield.reprime.models.request.web.rides.WayPointsData
import java.io.File

class CrashHandler(
    context: Context,
    private val breadcrumbTrailManager: BreadcrumbTrailManager,
) {
    private val applicationContext = context.applicationContext

    private var sharedPreferences =
        applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    /**
     * data#wayPointsData does not contain the origin. Therefore it needs to be added manually
     * before being used.
     */
    var data: Data? = null

    fun handleUncaughtException() {
        Log.e("bctpresist: ", "handleUncaughtException called")
        if (data?.isRecording == true) {
            if (breadcrumbTrailManager.isRecording) {
                breadcrumbTrailManager.pause(true) { breadcrumbTrail ->
                    storeBctData(breadcrumbTrail)
                }
            } else {
                data?.lastPausedBct?.let { storeBctData(it) }
            }
        }
    }

    private fun storeBctData(bct: BreadcrumbTrail) {
        storeBreadcrumbTrailToGpxFile(breadcrumbTrailManager, bct)
        data?.let { storeData(sharedPreferences, it) }
    }

    fun hasCrashed(): Boolean {
        val filePath = filePath()
        val file = File(filePath)
        return file.exists()
    }

    fun removeData() {
        removeCrashGpxFile()
        removeSharedPreferences()
    }

    private fun storeBreadcrumbTrailToGpxFile(
        breadcrumbTrailManager: BreadcrumbTrailManager,
        breadcrumbTrail: BreadcrumbTrail
    ) {
        val filePath = filePath()
        val file = File(filePath)

        breadcrumbTrailManager.exportToGpx(breadcrumbTrail, file)
    }

    @SuppressLint("ApplySharedPref") // intentionally done synchronous
    private fun storeData(sharedPreferences: SharedPreferences, data: Data) {
        val destination = data.destination

        val edit = sharedPreferences.edit()
        edit.putFloat("destination_lat", destination.latitude.toFloat())
        edit.putFloat("destination_lng", destination.longitude.toFloat())
        edit.putString("destination_name", data.wayPointsData.last().placeName)

        edit.putInt("number_of_waypoints", data.remainingWayPoints.size)

        edit.putBoolean("is_deeprecording_enabled", data.isDeepRecordingEnabled)

        data.remainingWayPoints.withIndex().forEach { indexedValue ->
            val index = indexedValue.index
            val waypoint = indexedValue.value
            val latLng = waypoint.latLng

            edit.putFloat("waypoint_${index}_lat", latLng.latitude.toFloat())
            edit.putFloat("waypoint_${index}_lng", latLng.longitude.toFloat())
            if (waypoint.isStopOver) {
                edit.putBoolean("waypoint_${index}_stopover", waypoint.isStopOver)
            }

            val wayPointsData = data.wayPointsData.find {
                it.latitude == latLng.latitude && it.longitude == latLng.longitude
            }
            edit.putString("waypoint_${index}_name", wayPointsData?.placeName)
        }

        edit.commit()
    }

    @SuppressLint("ApplySharedPref") // intentionally done synchronous
    fun loadData(): Data? {
        val destinationLat = sharedPreferences.getFloat("destination_lat", -1F)
        val destinationLng = sharedPreferences.getFloat("destination_lng", -1F)
        val destinationName = sharedPreferences.getString("destination_name", "Destination")
        if (destinationLat == -1F || destinationLng == -1F) {
            return null
        }

        val destination = LatLng(destinationLat.toDouble(), destinationLng.toDouble())

        val numberWayPoints = sharedPreferences.getInt("number_of_waypoints", 0)

        val wayPointsData = mutableListOf<WayPointsData>()
        val remainingWayPoints = mutableListOf<WayPoint>()
        for (i in 0..numberWayPoints) {
            val waypointLat = sharedPreferences.getFloat("waypoint_${i}_lat", -1F)
            val waypointLng = sharedPreferences.getFloat("waypoint_${i}_lng", -1F)
            val waypointStopover = sharedPreferences.getBoolean("waypoint_${i}_stopover", false)
            val waypointName = sharedPreferences.getString("waypoint_${i}_name", "Waypoint $i")

            if (waypointLat == -1F || waypointLng == -1F) {
                continue
            }

            val latLng = LatLng(waypointLat.toDouble(), waypointLng.toDouble())
            val wayPoint = WayPoint(latLng, waypointStopover)
            remainingWayPoints.add(wayPoint)

            val data = WayPointsData(
                waypointLat.toDouble(), waypointLng.toDouble(), waypointName
            )
            wayPointsData.add(data)
        }
        val isDeepRecordingEnabled = sharedPreferences.getBoolean("is_deeprecording_enabled", false)

        wayPointsData.add(
            WayPointsData(
                destinationLat.toDouble(),
                destinationLng.toDouble(),
                destinationName
            )
        )

        return Data(
            destination,
            remainingWayPoints,
            wayPointsData,
            isRecording = true,
            isDeepRecordingEnabled = isDeepRecordingEnabled
        )
    }

    private fun removeSharedPreferences() {
        applicationContext.deleteSharedPreferences(SHARED_PREFERENCES_NAME)
    }

    private fun removeCrashGpxFile() {
        val filePath = filePath()
        val file = File(filePath)

        if (file.exists()) {
            file.delete()
        }
    }

    private fun filePath() = applicationContext.filesDir.toString() + File.separator + FILE_NAME

    data class Data(
        val destination: LatLng,
        val remainingWayPoints: List<WayPoint>,
        /**
         * wayPointsData does not contain the origin. Therefore it needs to be added manually
         * before being used.
         */
        val wayPointsData: List<WayPointsData>,
        val isRecording: Boolean,
        val isDeepRecordingEnabled: Boolean,
        val lastPausedBct: BreadcrumbTrail? = null
    )

    private companion object {
        private const val TAG = "CrashHandler"
        private const val FILE_NAME = "crash.gpx"
        private const val SHARED_PREFERENCES_NAME = "crash"
    }
}
