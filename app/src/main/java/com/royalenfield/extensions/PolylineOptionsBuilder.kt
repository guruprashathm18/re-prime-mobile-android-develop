/*
 * Copyright (c) 2019 Bosch SoftTec GmbH All Rights Reserved.
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

package com.royalenfield.extensions

import android.util.Log
import androidx.annotation.ColorInt
import com.bosch.softtec.components.core.misc.logging.LogTag
import com.bosch.softtec.components.core.models.LatLng
import com.bosch.softtec.components.midgard.core.directions.models.CongestionType
import com.bosch.softtec.components.midgard.core.directions.models.EdgeInfo
import com.bosch.softtec.components.midgard.core.directions.models.RouteTrace
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.royalenfield.googleDirections.util.RouteTrafficColorFactory
import java.util.*
import kotlin.properties.Delegates

class PolylineOptionsBuilder
{
    private val routeTrafficColorFactory = RouteTrafficColorFactory()

    fun buildPolylineOptionsWithLiveTraffic(
        routeTrace: RouteTrace,
        routeTraceOptions: RouteTraceOptions
    ): List<PolylineOptions>
    {
        val polylineOptionsList = ArrayList<PolylineOptions>()

        var previousCongestionType: CongestionType? = null
        var currentPolylineOptions: PolylineOptions by Delegates.notNull()
        routeTrace.edgeInfos.forEach { edgeInfo ->
            val congestionType = edgeInfo.congestionType

            val isSameCongestionType = congestionType == previousCongestionType
            if (isSameCongestionType)
            {
                addToCurrentPolylineOptions(edgeInfo, currentPolylineOptions)
            }
            else
            {
                val routeTraceColor = routeTrafficColorFactory.getColorForCongestion(edgeInfo.congestionType)
                currentPolylineOptions = createNewPolylineOptions(edgeInfo, routeTraceColor)
                polylineOptionsList.add(currentPolylineOptions)

                previousCongestionType = congestionType
            }
        }

        Log.v(TAG, "Build ${polylineOptionsList.size} polylineOptions")

        return polylineOptionsList
    }

    fun buildPolylineOptionsForLiveTraffic(
        routeTrace: RouteTrace,
        routeTraceOptions: RouteTraceOptions
    ): List<PolylineOptions>
    {
        val colorInt = routeTrafficColorFactory.getColor(routeTraceOptions.isAlternative)

        return buildPolylineOptionsWithStaticColor(routeTrace, routeTraceOptions, colorInt)
    }

    private fun buildPolylineOptionsWithStaticColor(
        routeTrace: RouteTrace,
        routeTraceOptions: RouteTraceOptions,
        @ColorInt colorInt: Int
    ): List<PolylineOptions>
    {
        val polylineOptions = PolylineOptions()

        val polyline = routeTrace.polyline
        polyline.points.forEach {
            val latLng = createLatLng(it)
            polylineOptions.add(latLng)
        }

        val roundCap = RoundCap()

        polylineOptions
            .color(colorInt)
            .zIndex(routeTraceOptions.zIndex)
            .startCap(roundCap)
            .endCap(roundCap)
            .width(POLYLINE_WIDTH)
            .geodesic(IS_GEODESIC)
            .clickable(IS_CLICKABLE)

        return Collections.singletonList(polylineOptions)
    }

    private fun createLatLng(latLng: LatLng): com.google.android.gms.maps.model.LatLng =
        com.google.android.gms.maps.model.LatLng(latLng.latitude, latLng.longitude)

    private fun createNewPolylineOptions(
        edgeInfo: EdgeInfo,
        routeTraceColor: Int
    ): PolylineOptions
    {
        val startLatLng = createLatLng(edgeInfo.start)
        val endLatLng = createLatLng(edgeInfo.end)

        val roundCap = RoundCap()

        return PolylineOptions()
            .add(startLatLng)
            .add(endLatLng)
            .color(routeTraceColor)
            .zIndex(2.0F)
            .startCap(roundCap)
            .endCap(roundCap)
            .width(POLYLINE_WIDTH)
            .geodesic(IS_GEODESIC)
            .clickable(IS_CLICKABLE)
    }

    private fun addToCurrentPolylineOptions(
        edgeInfo: EdgeInfo,
        polylineOptions: PolylineOptions
    )
    {
        val endLatLng = createLatLng(edgeInfo.end)

        polylineOptions.add(endLatLng)
    }

    private companion object
    {
        private val TAG = LogTag.of(PolylineOptionsBuilder::class.java)

        const val POLYLINE_WIDTH = 20F
        const val IS_GEODESIC = true
        const val IS_CLICKABLE = true
    }
}
