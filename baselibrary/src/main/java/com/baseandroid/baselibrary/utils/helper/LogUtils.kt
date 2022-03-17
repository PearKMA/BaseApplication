package com.baseandroid.baselibrary.utils.helper

import android.content.Context
import com.baseandroid.baselibrary.utils.formatTimePattern
import com.baseandroid.baselibrary.utils.getOutputFileDirectory
import java.io.File

fun Context.writeLog(message: String) {
    try {
        val file = File(getOutputFileDirectory(this), "log.txt")
        val currentTime = formatTimePattern(System.currentTimeMillis(), "dd/MM/yyyy - hh:mm")
        file.printWriter().use { out ->
            out.println("$currentTime : $message")
        }
    } catch (e: Exception) {
    }
}