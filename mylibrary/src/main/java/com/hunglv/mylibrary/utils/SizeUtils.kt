package com.hunglv.mylibrary.utils

import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

fun readableFileSize(l: Long): String {
    if (l <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(l.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.##").format(
        l / 1024.0.pow(digitGroups.toDouble())
    ).toString() + "" + units[digitGroups]
}