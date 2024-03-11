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

import com.bosch.softtec.components.midgard.core.directions.models.CongestionType
import com.bosch.softtec.components.midgard.core.directions.models.RouteTrace
import com.google.android.gms.maps.GoogleMap

/**
 * @author Andre Weber
 * @since 2019-05-13
 */

data class RouteTraceOptions(
        val isAlternative: Boolean,
        val isBaseRoute: Boolean = false,
        val zIndex: Float = if (isAlternative) 1.0F else 2.0F
)

private val polylineOptionBuilder =
        PolylineOptionsBuilder()


fun drawRouteTrace(
        isSupportingVisualCongestion: Boolean,
        routeTrace: RouteTrace,
        routeTraceOptions: RouteTraceOptions,
        googleMap: GoogleMap
) {
    val routeCongestionIsNonNull = routeTrace.edgeInfos.all { it.congestionType != null }
    val routeHasAtLeastOneKnownCongestion =
            routeTrace.edgeInfos.any { it.congestionType != null && it.congestionType != CongestionType.UNKNOWN }
    val routeHasLiveTraffic = routeCongestionIsNonNull && routeHasAtLeastOneKnownCongestion

    val isAlternative = routeTraceOptions.isAlternative

    val polylineOptionsList = if (isSupportingVisualCongestion && !isAlternative && routeHasLiveTraffic) {
        polylineOptionBuilder.buildPolylineOptionsWithLiveTraffic(routeTrace, routeTraceOptions)
    } else {
        polylineOptionBuilder.buildPolylineOptionsForLiveTraffic(routeTrace, routeTraceOptions)
    }

    polylineOptionsList.forEach {
        val polyline = googleMap.addPolyline(it)
    }
}