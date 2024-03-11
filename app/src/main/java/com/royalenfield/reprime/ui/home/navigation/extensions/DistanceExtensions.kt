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
import com.bosch.softtec.components.core.conversion.LengthUnit
import com.bosch.softtec.components.core.models.Distance
import com.royalenfield.bluetooth.util.TbtType
import com.royalenfield.reprime.R
import com.royalenfield.reprime.configuration.ConfigurationPrefManager
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
fun Distance.prettifyMetric(context: Context): String {
    if (toMeters() < 1) return ""

    val roundedDistanceForMeterScope = round(this, LengthUnit.METERS)
    return if (roundedDistanceForMeterScope.toMeters() < 1000) {
        context.getString(R.string.distance_x_m, "" + roundedDistanceForMeterScope.toMeters().toInt())
    } else {
        val roundedDistanceForKilometerScope = round(this, LengthUnit.KILOMETERS)
        context.getString(
            R.string.distance_x_km,
            formatBigValues(roundedDistanceForKilometerScope.toKilometers())
        )
    }
}

fun Distance.prettifyMetricNumber(): Int {

    val roundedDistanceForMeterScope = round(this, LengthUnit.METERS)

    return roundedDistanceForMeterScope.toMeters().toInt()

}

fun Distance.prettifyImpericalNumber(): Int {

    val roundedDistanceForMeterScope = round(this, LengthUnit.FEET)

    return roundedDistanceForMeterScope.toFeet().toInt()

}

/**
 * Generates a audio-friendly representation of the distance in the metric-form.
 *
 * @param context
 *      needed to get some string resources
 *
 * @return the audio-friendly String with either meter or kilometer values.
 */
fun Distance.prettifyMetricForVoice(context: Context, withIn: Boolean = true): String {
    if (toMeters() <= 100) return ""

    val roundedDistanceForMeterScope = round(this, LengthUnit.METERS)
    return if (roundedDistanceForMeterScope.toMeters() <= 1000)
    {
        if (roundedDistanceForMeterScope.toMeters() == 1000.0)
            if (withIn) context.getString(R.string.distance_in_x_kilometer, context.getString(R.string.one))
            else context.getString(R.string.distance_x_kilometer, context.getString(R.string.one))
        else
            if (withIn) context.getString(
                R.string.distance_in_x_meter,
                roundedDistanceForMeterScope.toMeters().toInt().toString()
            )
            else context.getString(
                R.string.distance_in_x_meter,
                roundedDistanceForMeterScope.toMeters().toInt().toString()
            )
    }
    else
    {
        val roundedDistanceForKilometerScope = round(this, LengthUnit.KILOMETERS)
        if (withIn)
            context.getString(
                R.string.distance_in_x_kilometer,
                formatBigValues(roundedDistanceForKilometerScope.toKilometers())
            )
        else
            context.getString(
                R.string.distance_x_kilometer,
                formatBigValues(roundedDistanceForKilometerScope.toKilometers())
            )
    }
}

/**
 * Generates a UI-friendly representation of the distance in the imperial-form.
 *
 * @param context
 *      needed to get some string resources
 *
 * @return the UI-friendly String with either feet or mile values.
 */
fun Distance.prettifyImperial(context: Context): String {
    if (toFeet() < 1) return ""

    val roundedDistanceForFeetScope = round(this, LengthUnit.FEET)
    return if (roundedDistanceForFeetScope.toFeet() < 500) {
        context.getString(R.string.distance_x_ft, "" + roundedDistanceForFeetScope.toFeet().toInt())
    } else {
        val roundedDistanceForMilesScope = round(this, LengthUnit.MILES)
        context.getString(
            R.string.distance_x_mi,
            formatBigValues(roundedDistanceForMilesScope.toMiles())
        )
    }
}

/**
 * Generates a audio-friendly representation of the distance in the imperial-form.
 *
 * @param context
 *      needed to get some string resources
 *
 * @return the audio-friendly String with either feet or mile values.
 */
fun Distance.prettifyImperialForVoice(context: Context, withIn: Boolean = true): String {
    if (toMeters() < 1) return ""

    val roundedDistanceForFeetScope = round(this, LengthUnit.FEET)

    if (roundedDistanceForFeetScope.toFeet() <= 300) {
        return context.getString(R.string.distance_now)
    }

    return if (roundedDistanceForFeetScope.toFeet() <= 500) {
        if (withIn) context.getString(
            R.string.distance_in_x_feet,
            roundedDistanceForFeetScope.toFeet().toInt().toString()
        )
        else context.getString(R.string.distance_x_feet, roundedDistanceForFeetScope.toFeet().toInt().toString())
    } else {
        val roundedDistanceForMileScope = round(this, LengthUnit.MILES)
        if (withIn) context.getString(
            R.string.distance_in_x_miles,
            formatBigValues(roundedDistanceForMileScope.toMiles())
        )
        else context.getString(
            R.string.distance_x_miles,
            formatBigValues(roundedDistanceForMileScope.toMiles())
        )
    }
}

fun getDecrementValueForDistance(distance: Int): Int {
    return when (distance) {
        in 1000..10000000 -> 100
        in 101..999 -> 50
        in 0..100 -> 10
        else -> 0
    }
}

fun getTbt1ProgressLevel(distance: Int, tbt1Id: Int): Int {
    return when (distance) {
        in 0..20 -> 4 // flash
        in 20..40 -> if (tbt1Id == TbtType.KEEP_LEFT || tbt1Id == TbtType.KEEP_RIGHT) 4 else 3
        in 40..60 -> if (tbt1Id == TbtType.KEEP_LEFT || tbt1Id == TbtType.KEEP_RIGHT) 4 else 2
        in 60..80 -> if (tbt1Id == TbtType.KEEP_LEFT || tbt1Id == TbtType.KEEP_RIGHT) 4 else 1
        in 80..100 -> if (tbt1Id == TbtType.KEEP_LEFT || tbt1Id == TbtType.KEEP_RIGHT) 4 else 0
        in 101..500 -> 6 // red full


        else -> 5 // orange
    }
}

fun getTbt2ProgressLevel(distance: Int?, configurationPrefManager: ConfigurationPrefManager?): Int {
    return when (distance) {
        in 1..configurationPrefManager!!.minDistance2ndTbt2RED -> 2 // red

        else -> 1 // black
    }
}



/**
 * Formats big values (kilometers and miles) into a String, which can be used to show on UI.
 *
 * @param roundedBigDistance
 *      a distance value in form of kilometers or miles
 *
 * @return a formatted distance (either with or without decimal places)
 */
private fun formatBigValues(roundedBigDistance: Double): String {
    val pattern = if (roundedBigDistance <= 100.0) "#.#" else "#"
    val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault())
    val format = DecimalFormat(pattern, decimalFormatSymbols)
    return format.format(roundedBigDistance)
}

/**
 * Rounds the given distance to a UI-friendly value in the requested [LengthUnit].
 *
 * @param distance
 *      the distance to round up
 * @param lengthUnit
 *      the lengthUnit to round up the distance to
 * @return rounded up distance for the requested lengthUnit
 */
private fun round(distance: Distance, lengthUnit: LengthUnit): Distance {
    when (lengthUnit) {
        LengthUnit.METERS -> {
            val roundedSmallDistance = roundSmallValues(distance.toMeters())
            return Distance(roundedSmallDistance, LengthUnit.METERS)
        }
        LengthUnit.FEET -> {
            val roundedSmallDistance = roundSmallValues(distance.toFeet())
            return Distance(roundedSmallDistance, LengthUnit.FEET)
        }
        LengthUnit.KILOMETERS -> {
            val roundedBigDistance = roundBigValues(distance.toKilometers())
            return Distance(roundedBigDistance, LengthUnit.KILOMETERS)
        }
        LengthUnit.MILES -> {
            val roundedBigDistance = roundBigValues(distance.toMiles())
            return Distance(roundedBigDistance, LengthUnit.MILES)
        }
    }
}


/**
 * Rounds the given value to slightly rounded up values, which then can be used to show on UI.
 * Values below 100 will be rounded up to next ten-value (e.g. 33 -> 40).
 * Values above 100 will be rounded up to next fifty-value (e.g. 432 -> 450).
 *
 * @param distance
 *      the distance to round up
 * @return rounded up distance
 */
private fun roundSmallValues(distance: Double): Double {
    val intDistance = distance.toInt()
    return when {
        intDistance <= 100 -> roundToNextUpperTen(intDistance)
        else -> roundToNextUpperFifties(intDistance)
    }
}

/**
 * Rounds the given value to slightly rounded up values, which then can be used to show on UI.
 * Values below 100 (thought for kilometers and miles) will be rounded up to next decimal place (e.g. 34.232 -> 34.3).
 * Values above 100 will be rounded up to next full-value (e.g. 134.232 -> 135).
 *
 * @param distance
 *      the distance to round up
 * @return rounded up distance
 */
private fun roundBigValues(distance: Double): Double {
    val distanceMultipliedByThousand = distance * 1000
    val intDistanceMultipliedByThousand = distanceMultipliedByThousand.toInt()
    val roundedDistanceMultipliedByThousand = when {
        intDistanceMultipliedByThousand < 100000 -> roundToNextUpperHundred(
            intDistanceMultipliedByThousand
        )
        else -> roundToNextThousand(
            intDistanceMultipliedByThousand
        )
    }

    return roundedDistanceMultipliedByThousand / 1000
}

/**
 * Rounds a given value to next thousand-km-values (e.g.: 432 -> 1000, 1330 -> 2000, 2640 -> 3000).
 *
 * @param distance
 * the given distance, which should be go to 1000-value.
 * @return the rounded value.
 */
@VisibleForTesting
internal fun roundToNextThousand(distance: Int): Double {
    return if (distance < 0) 0.0 else ((distance + 999) / 1000 * 1000).toDouble()
}

/**
 * Rounds a given value to the next upper 50 (e.g.: 432 -> 450, 330 -> 350).
 *
 * @param distance
 * the given distance, which should be go to the next 50-value.
 * @return the rounded up value.
 */
@VisibleForTesting
internal fun roundToNextUpperFifties(distance: Int): Double {
    return if (distance < 0) 0.0 else ((distance + 49) / 50 * 50).toDouble()
}

/**
 * Rounds a given value to the next upper 100 (e.g.: 432 -> 500, 330 -> 400).
 *
 * @param distance
 * the given distance, which should be go to the next 100-value.
 * @return the rounded up value.
 */
@VisibleForTesting
internal fun roundToNextUpperHundred(distance: Int): Double {
    return if (distance < 0) 0.0 else ((distance + 99) / 100 * 100).toDouble()
}

/**
 * Rounds a given value to the next upper 10 (e.g.: 432 -> 440, 430 -> 430).
 *
 * @param distance
 * the given distance, which should be go to the next 10-value.
 * @return the rounded up value.
 */
@VisibleForTesting
internal fun roundToNextUpperTen(distance: Int): Double {
    return if (distance < 0) 0.0 else ((distance + 9) / 10 * 10).toDouble()
}

