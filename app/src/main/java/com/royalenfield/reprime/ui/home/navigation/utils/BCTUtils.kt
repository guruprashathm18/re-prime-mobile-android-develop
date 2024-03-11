package com.royalenfield.reprime.ui.home.navigation.utils

import com.bosch.softtec.components.core.models.Distance
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils

/**
 * Created by Venkat on 3/1/2022.
 */
object BCTUtils {

    /**
     * Checks the BCT status from the object fetched from Firestore remote configuration based on region
     * @return Boolean Returns true if enabled
     */
    fun isBCTFeatureEnabled(): Boolean {
        val feature = REUtils.getFeatureMap()[REApplication.getInstance().Country] ?: return false
        return feature.isBCTEnabled.equals(REConstants.FEATURE_ENABLED, ignoreCase = true)
    }

    fun getTrailRecordLimit(): String {
            return REUtils.getNavigationKeys().trailRecordLimit
    }

    fun getDistance(distance: Distance): String {
        val strDistance: String = if (REUtils.isMilesUnitSelected()) {
            REUtils.formatDistanceInMilesUnit(distance)
        } else {
            REUtils.getDistanceInUnits(distance.distanceValue)
        }
        return strDistance
    }
}