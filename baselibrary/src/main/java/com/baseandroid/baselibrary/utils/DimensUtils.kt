package com.baseandroid.baselibrary.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt

fun Float.convertDpToPixel(): Int {
    val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
    val px = this * (metrics.densityDpi / 160f)
    return px.roundToInt()
}

fun hasNotch(context: Context): Boolean {
    var statusBarHeight = 0
    val resourceId = context.resources.getIdentifier(
        "status_bar_height",
        "dimen",
        "android"
    )
    if (resourceId > 0) {
        statusBarHeight = context.resources.getDimensionPixelOffset(resourceId)
    }
    val barHeight = 24f.convertDpToPixel()

    return statusBarHeight > barHeight
}

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels