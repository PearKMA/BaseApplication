package com.baseandroid.baseapplication.utils

import android.content.Context
import android.graphics.Color


fun evaluateTransitionColor(fraction: Float, startValue: Int, endValue: Int): Int {
    val startA = startValue shr 24 and 0xff
    val startR = startValue shr 16 and 0xff
    val startG = startValue shr 8 and 0xff
    val startB = startValue and 0xff
    val endA = endValue shr 24 and 0xff
    val endR = endValue shr 16 and 0xff
    val endG = endValue shr 8 and 0xff
    val endB = endValue and 0xff
    return startA + (fraction * (endA - startA)).toInt() shl 24 or
            (startR + (fraction * (endR - startR)).toInt() shl 16) or
            (startG + (fraction * (endG - startG)).toInt() shl 8) or
            startB + (fraction * (endB - startB)).toInt()
}

fun Context.generateRandomColours(): MutableList<Int> {
    return mutableListOf(
        Color.parseColor("#673AB7"),
        Color.parseColor("#3F51B5"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#03A9F4"),
        Color.parseColor("#00BCD4"),
        Color.parseColor("#009688"),
        Color.parseColor("#8BC34A"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#FF5722"),
        Color.parseColor("#F44336")
    )
}