package com.baseandroid.baselibrary.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

val Number.toPixel
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

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
    val barHeight = 24f.toPixel.toInt()

    return statusBarHeight > barHeight
}

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels