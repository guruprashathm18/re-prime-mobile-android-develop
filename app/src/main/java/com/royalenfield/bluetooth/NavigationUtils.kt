package com.royalenfield.bluetooth

import com.bosch.softtec.components.midgard.core.directions.models.Maneuver
import com.bosch.softtec.components.midgard.core.directions.models.ManeuverType
import com.royalenfield.bluetooth.util.TbtType

/**
 * @author BOP1KOR on 5/17/2019.
 *
 */

fun getRemappedManeuverId(maneuver: Maneuver?, maneuverType: ManeuverType): Int {

    return when (maneuverType) {

        ManeuverType.DEPART -> TbtType.DEPART
        ManeuverType.ARRIVE -> TbtType.ARRIVED_AT_DESTINATION
        ManeuverType.ARRIVE_LEFT -> TbtType.ARRIVED_AT_DESTINATION_LEFT
        ManeuverType.ARRIVE_RIGHT -> TbtType.ARRIVED_AT_DESTINATION_RIGHT
        ManeuverType.MERGE -> TbtType.MERGE_RIGHT // TODO
        ManeuverType.MERGE_LEFT -> TbtType.MERGE_LEFT
        ManeuverType.MERGE_RIGHT -> TbtType.MERGE_RIGHT
        ManeuverType.MERGE_SLIGHT_LEFT -> TbtType.MERGE_SLIGHT_LEFT
        ManeuverType.MERGE_SLIGHT_RIGHT -> TbtType.MERGE_SLIGHT_RIGHT
        ManeuverType.FORK_LEFT -> TbtType.FORK_LEFT
        ManeuverType.FORK_RIGHT -> TbtType.FORK_RIGHT
        ManeuverType.FORK_SLIGHT_LEFT -> TbtType.FORK_SLIGHT_LEFT
        ManeuverType.FORK_SLIGHT_RIGHT -> TbtType.FORK_SLIGHT_RIGHT
        ManeuverType.KEEP_LEFT -> TbtType.KEEP_LEFT
        ManeuverType.KEEP_RIGHT -> TbtType.KEEP_RIGHT
        ManeuverType.ONRAMP -> TbtType.ON_RAMP
        ManeuverType.ONRAMP_LEFT -> TbtType.ONRAMP_LEFT
        ManeuverType.ONRAMP_RIGHT -> TbtType.ONRAMP_RIGHT
        ManeuverType.ONRAMP_SLIGHT_LEFT -> TbtType.ONRAMP_SLIGHT_LEFT
        ManeuverType.ONRAMP_SLIGHT_RIGHT -> TbtType.ONRAMP_SLIGHT_RIGHT
        ManeuverType.OFFRAMP_LEFT -> TbtType.OFF_RAMP_LEFT
        ManeuverType.OFFRAMP_RIGHT -> TbtType.OFF_RAMP_RIGHT
        ManeuverType.OFFRAMP_SLIGHT_LEFT -> TbtType.OFF_RAMP_SLIGHT_LEFT
        ManeuverType.OFFRAMP_SLIGHT_RIGHT -> TbtType.OFF_RAMP_SLIGHT_RIGHT
        ManeuverType.ROUNDABOUT_LEFT -> TbtType.ROUNDABOUT_LEFT
        ManeuverType.ROUNDABOUT_RIGHT -> TbtType.ROUND_ABOUT_RIGHT
        ManeuverType.STRAIGHT -> TbtType.STRAIGHT
        ManeuverType.LEFT -> TbtType.TURN_LEFT
        ManeuverType.RIGHT -> TbtType.TURN_RIGHT
        ManeuverType.SLIGHT_LEFT -> TbtType.TURN_SLIGHT_LEFT
        ManeuverType.SLIGHT_RIGHT -> TbtType.TURN_SLIGHT_RIGHT
        ManeuverType.SHARP_LEFT -> TbtType.TURN_SHARP_LEFT
        ManeuverType.SHARP_RIGHT -> TbtType.TURN_SHARP_RIGHT
        ManeuverType.U_TURN_LEFT -> TbtType.U_TURN_LEFT
        ManeuverType.U_TURN_RIGHT -> TbtType.U_Turn_RIGHT
        ManeuverType.FERRY -> TbtType.FERRY
        ManeuverType.FERRY_TRAIN -> TbtType.FERRY_TRAIN
        ManeuverType.CONTINUE -> TbtType.CONTINUE
        ManeuverType.ONRAMP_SHARP_LEFT -> TbtType.ONRAMP_SHARP_LEFT
        ManeuverType.ONRAMP_SHARP_RIGHT -> TbtType.ONRAMP_SHARP_RIGHT

        /*ManeuverType.NOTHING -> {
            var ret = -1
            parseNothingUnknown(maneuver, ret)
        }*/
        ManeuverType.NOTHING -> TbtType.NOTHING
        ManeuverType.UNKNOWN -> {
            var ret = -1
            parseNothingUnknown(maneuver, ret)
        }

    }
}

private fun parseNothingUnknown(maneuver: Maneuver?, ret: Int): Int {
    var ret1 = ret
    if (maneuver?.visualInstruction!!.contains("continue") || maneuver?.visualInstruction!!.contains("follow")) {
        ret1 = 9
    } else if (maneuver?.visualInstruction!!.contains("slight")) {
        if (maneuver?.visualInstruction!!.contains("right")) {
            ret1 = 25
        } else if (maneuver?.visualInstruction!!.contains("left")) {
            ret1 = 24
        }
    } else if (maneuver?.visualInstruction!!.contains("sharp")) {
        if (maneuver?.visualInstruction!!.contains("right")) {
            ret1 = 23
        } else if (maneuver?.visualInstruction!!.contains("left")) {
            ret1 = 22
        }

    } else if (maneuver?.visualInstruction!!.contains("turns")) {

        if (maneuver?.visualInstruction!!.contains("right")) {
            ret1 = 21

        } else if (maneuver?.visualInstruction!!.contains("left")) {
            ret1 = 20
        }
    }
    return ret1
}

fun getProgressLevel(distance: Int): Int {
    return when (distance) {
        in 0..20 -> 4 // flash
        in 20..40 -> 3
        in 40..60 -> 2
        in 60..80 -> 1
        in 80..100 -> 0
        in 101..500 -> 6 // red full
        else -> 5 // orange
    }
}
