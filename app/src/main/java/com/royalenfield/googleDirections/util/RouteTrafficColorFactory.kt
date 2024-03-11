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

package com.royalenfield.googleDirections.util

import androidx.annotation.ColorInt
import com.bosch.softtec.components.midgard.core.directions.models.CongestionType
import com.royalenfield.googleDirections.util.RouteTrafficColorFactory.NonTrafficColors.ALTERNATIVE_ROUTE_COLOR
import com.royalenfield.googleDirections.util.RouteTrafficColorFactory.NonTrafficColors.MAIN_ROUTE_COLOR
import com.royalenfield.googleDirections.util.RouteTrafficColorFactory.TrafficColors.HIGH_TRAFFIC_COLOR
import com.royalenfield.googleDirections.util.RouteTrafficColorFactory.TrafficColors.LOW_TRAFFIC_COLOR
import com.royalenfield.googleDirections.util.RouteTrafficColorFactory.TrafficColors.MEDIUM_TRAFFIC_COLOR
import com.royalenfield.googleDirections.util.RouteTrafficColorFactory.TrafficColors.NO_TRAFFIC_COLOR
import com.royalenfield.googleDirections.util.RouteTrafficColorFactory.TrafficColors.UNKNOWN_TRAFFIC_COLOR

internal class RouteTrafficColorFactory {
    fun getColorForCongestion(congestionType: CongestionType?): Int {
        return when (congestionType) {
            null,
            CongestionType.UNKNOWN -> UNKNOWN_TRAFFIC_COLOR

            CongestionType.NO -> NO_TRAFFIC_COLOR

            CongestionType.LOW -> LOW_TRAFFIC_COLOR

            CongestionType.MEDIUM -> MEDIUM_TRAFFIC_COLOR

            CongestionType.HIGH -> HIGH_TRAFFIC_COLOR
        }
    }

    fun getColor(isAlternative: Boolean): Int {
        return if (isAlternative) ALTERNATIVE_ROUTE_COLOR else MAIN_ROUTE_COLOR
    }

    private object TrafficColors {
        @ColorInt
        const val UNKNOWN_TRAFFIC_COLOR = 0xFF888888.toInt() // grey - black

        @ColorInt
        const val NO_TRAFFIC_COLOR = 0xFF0098D9.toInt() // blue

        @ColorInt
        const val LOW_TRAFFIC_COLOR = 0xFF0098D9.toInt() // blue

        @ColorInt
        const val MEDIUM_TRAFFIC_COLOR = 0xFFF05136.toInt() // orange

        @ColorInt
        const val HIGH_TRAFFIC_COLOR = 0xFF800000.toInt() // red
    }

    private object NonTrafficColors {
        @ColorInt
        const val MAIN_ROUTE_COLOR = 0xFF0098D9.toInt() // blue

        @ColorInt
        const val ALTERNATIVE_ROUTE_COLOR = 0xFFAFBFCF.toInt() // grey
    }
}
