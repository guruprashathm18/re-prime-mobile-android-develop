package com.royalenfield.reprime.ui.home.homescreen.customviews

import android.content.Context

fun Context.dp(valueInDp: Int): Int = (valueInDp * resources.displayMetrics.density).toInt()