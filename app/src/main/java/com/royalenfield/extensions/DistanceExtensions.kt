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

import android.content.Context
import androidx.annotation.VisibleForTesting
//import com.bosch.softtec.components.midgard.core.common.conversion.LengthUnit
//import com.bosch.softtec.components.midgard.core.common.models.Distance
import com.royalenfield.reprime.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * @author Martin Hellwig
 * @since  2018-12-19
 */
/**
 * Generates a UI-friendly representation of the distance in the metric-form.
 *
 * @param context
 *      needed to get some string resources
 *
 * @return the UI-friendly String with either meter or kilometer values.
 */
//fun Distance.prettifyMetric(context: Context): String {
//    if (toMeter() < 1) return ""
//
//    val roundedDistanceForMeterScope = round(this, LengthUnit.METER)
//    return if (roundedDistanceForMeterScope.toMeter() < 1000) {
//        context.getString(R.string.distance_x_m, "" + roundedDistanceForMeterScope.toMeter().toInt())
//    } else {
//        val roundedDistanceForKilometerScope = round(this, LengthUnit.KILOMETER)
//        context.getString(
//            R.string.distance_x_km,
//            formatBigValues(roundedDistanceForKilometerScope.toKilometer())
//        )
//    }
//}
//
//fun Distance.prettifyMetricNumber(): Int {
//
//    val roundedDistanceForMeterScope = round(this, LengthUnit.METER)
//
//    return roundedDistanceForMeterScope.toMeter().toInt()
//
//}
//
///**
// * Generates a audio-friendly representation of the distance in the metric-form.
// *
// * @param context
// *      needed to get some string resources
// *
// * @return the audio-friendly String with either meter or kilometer values.
// */
//fun Distance.prettifyMetricForVoice(context: Context, withIn: Boolean = true): String {
//    if (toMeter() < 1) return ""
//
//    val roundedDistanceForMeterScope = round(this, LengthUnit.METER)
//
//    if (roundedDistanceForMeterScope.toMeter() <= 100) {
//        return context.getString(R.string.distance_now)
//    }
//
//    return if (roundedDistanceForMeterScope.toMeter() <= 1000) {
//        if (roundedDistanceForMeterScope.toMeter() == 1000.0)
//            if (withIn) context.getString(R.string.distance_in_x_kilometer, context.getString(R.string.one))
//            else context.getString(R.string.distance_x_kilometer, context.getString(R.string.one))
//        else
//            if (withIn) context.getString(
//                R.string.distance_in_x_meter,
//                roundedDistanceForMeterScope.toMeter().toInt().toString()
//            )
//            else context.getString(
//                R.string.distance_in_x_meter,
//                roundedDistanceForMeterScope.toMeter().toInt().toString()
//            )
//    } else {
//        val roundedDistanceForKilometerScope = round(this, LengthUnit.KILOMETER)
//        if (withIn)
//            context.getString(
//                R.string.distance_in_x_kilometer,
//                formatBigValues(roundedDistanceForKilometerScope.toKilometer())
//            )
//        else
//            context.getString(
//                R.string.distance_x_kilometer,
//                formatBigValues(roundedDistanceForKilometerScope.toKilometer())
//            )
//    }
//}
//
///**
// * Generates a UI-friendly representation of the distance in the imperial-form.
// *
// * @param context
// *      needed to get some string resources
// *
// * @return the UI-friendly String with either feet or mile values.
// */
//fun Distance.prettifyImperial(context: Context): String {
//    if (toFeet() < 1) return ""
//
//    val roundedDistanceForFeetScope = round(this, LengthUnit.FEET)
//    return if (roundedDistanceForFeetScope.toFeet() < 500) {
//        context.getString(R.string.distance_x_ft, "" + roundedDistanceForFeetScope.toFeet().toInt())
//    } else {
//        val roundedDistanceForMilesScope = round(this, LengthUnit.MILES)
//        context.getString(
//            R.string.distance_x_mi,
//            formatBigValues(roundedDistanceForMilesScope.toMiles())
//        )
//    }
//}
//
///**
// * Generates a audio-friendly representation of the distance in the imperial-form.
// *
// * @param context
// *      needed to get some string resources
// *
// * @return the audio-friendly String with either feet or mile values.
// */
//fun Distance.prettifyImperialForVoice(context: Context, withIn: Boolean = true): String {
//    if (toMeter() < 1) return ""
//
//    val roundedDistanceForFeetScope = round(this, LengthUnit.FEET)
//
//    if (roundedDistanceForFeetScope.toFeet() <= 300) {
//        return context.getString(R.string.distance_now)
//    }
//
//    return if (roundedDistanceForFeetScope.toFeet() <= 500) {
//        if (withIn) context.getString(
//            R.string.distance_in_x_feet,
//            roundedDistanceForFeetScope.toFeet().toInt().toString()
//        )
//        else context.getString(R.string.distance_x_feet, roundedDistanceForFeetScope.toFeet().toInt().toString())
//    } else {
//        val roundedDistanceForMileScope = round(this, LengthUnit.MILES)
//        if (withIn) context.getString(
//            R.string.distance_in_x_miles,
//            formatBigValues(roundedDistanceForMileScope.toMiles())
//        )
//        else context.getString(
//            R.string.distance_x_miles,
//            formatBigValues(roundedDistanceForMileScope.toMiles())
//        )
//    }
//}
//
///**
// * Formats big values (kilometers and miles) into a String, which can be used to show on UI.
// *
// * @param roundedBigDistance
// *      a distance value in form of kilometers or miles
// *
// * @return a formatted distance (either with or without decimal places)
// */
//private fun formatBigValues(roundedBigDistance: Double): String {
//    val pattern = if (roundedBigDistance <= 100.0) "#.#" else "#"
//    val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault())
//    val format = DecimalFormat(pattern, decimalFormatSymbols)
//    return format.format(roundedBigDistance)
//}
//
///**
// * Rounds the given distance to a UI-friendly value in the requested [LengthUnit].
// *
// * @param distance
// *      the distance to round up
// * @param lengthUnit
// *      the lengthUnit to round up the distance to
// * @return rounded up distance for the requested lengthUnit
// */
//private fun round(distance: Distance, lengthUnit: LengthUnit): Distance {
//    when (lengthUnit) {
//        LengthUnit.METER -> {
//            val roundedSmallDistance = roundSmallValues(distance.toMeter())
//            return Distance(roundedSmallDistance, LengthUnit.METER)
//        }
//        LengthUnit.FEET -> {
//            val roundedSmallDistance = roundSmallValues(distance.toFeet())
//            return Distance(roundedSmallDistance, LengthUnit.FEET)
//        }
//        LengthUnit.KILOMETER -> {
//            val roundedBigDistance = roundBigValues(distance.toKilometer())
//            return Distance(roundedBigDistance, LengthUnit.KILOMETER)
//        }
//        LengthUnit.MILES -> {
//            val roundedBigDistance = roundBigValues(distance.toMiles())
//            return Distance(roundedBigDistance, LengthUnit.MILES)
//        }
//    }
//}
//
///**
// * Rounds the given value to slightly rounded up values, which then can be used to show on UI.
// * Values below 100 will be rounded up to next ten-value (e.g. 33 -> 40).
// * Values above 100 will be rounded up to next fifty-value (e.g. 432 -> 450).
// *
// * @param distance
// *      the distance to round up
// * @return rounded up distance
// */
//private fun roundSmallValues(distance: Double): Double {
//    val intDistance = distance.toInt()
//    return when {
//        intDistance <= 100 -> roundToNextUpperTen(intDistance)
//        else -> roundToNextUpperFifties(intDistance)
//    }
//}
//
///**
// * Rounds the given value to slightly rounded up values, which then can be used to show on UI.
// * Values below 100 (thought for kilometers and miles) will be rounded up to next decimal place (e.g. 34.232 -> 34.3).
// * Values above 100 will be rounded up to next full-value (e.g. 134.232 -> 135).
// *
// * @param distance
// *      the distance to round up
// * @return rounded up distance
// */
//private fun roundBigValues(distance: Double): Double {
//    val distanceMultipliedByThousand = distance * 1000
//    val intDistanceMultipliedByThousand = distanceMultipliedByThousand.toInt()
//    val roundedDistanceMultipliedByThousand = when {
//        intDistanceMultipliedByThousand < 100000 -> roundToNextUpperHundred(
//            intDistanceMultipliedByThousand
//        )
//        else -> roundToNextThousand(
//            intDistanceMultipliedByThousand
//        )
//    }
//
//    return roundedDistanceMultipliedByThousand / 1000
//}
//
///**
// * Rounds a given value to next thousand-km-values (e.g.: 432 -> 1000, 1330 -> 2000, 2640 -> 3000).
// *
// * @param distance
// * the given distance, which should be go to 1000-value.
// * @return the rounded value.
// */
//@VisibleForTesting
//internal fun roundToNextThousand(distance: Int): Double {
//    return if (distance < 0) 0.0 else ((distance + 999) / 1000 * 1000).toDouble()
//}
//
///**
// * Rounds a given value to the next upper 50 (e.g.: 432 -> 450, 330 -> 350).
// *
// * @param distance
// * the given distance, which should be go to the next 50-value.
// * @return the rounded up value.
// */
//@VisibleForTesting
//internal fun roundToNextUpperFifties(distance: Int): Double {
//    return if (distance < 0) 0.0 else ((distance + 49) / 50 * 50).toDouble()
//}
//
///**
// * Rounds a given value to the next upper 100 (e.g.: 432 -> 500, 330 -> 400).
// *
// * @param distance
// * the given distance, which should be go to the next 100-value.
// * @return the rounded up value.
// */
//@VisibleForTesting
//internal fun roundToNextUpperHundred(distance: Int): Double {
//    return if (distance < 0) 0.0 else ((distance + 99) / 100 * 100).toDouble()
//}
//
///**
// * Rounds a given value to the next upper 10 (e.g.: 432 -> 440, 430 -> 430).
// *
// * @param distance
// * the given distance, which should be go to the next 10-value.
// * @return the rounded up value.
// */
//@VisibleForTesting
//internal fun roundToNextUpperTen(distance: Int): Double {
//    return if (distance < 0) 0.0 else ((distance + 9) / 10 * 10).toDouble()
//}
