package com.baseandroid.baselibrary.utils

import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

fun readableFileSize(l: Long): String {
    if (l <= 0) return "0B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(l.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.##").format(
        l / 1024.0.pow(digitGroups.toDouble())
    ).toString() + "" + units[digitGroups]
}

fun folderSize(file: File?): Long {
    if (file == null || !file.exists())
        return 0
    //
    if (!file.isDirectory && !file.isHidden)
        return file.length()
    val dirs = LinkedList<File>()
    dirs.add(file)
    var result = 0L
    while (!dirs.isEmpty()) {
        val dir = dirs.removeAt(0)
        if (!dir.exists())
            continue
        val listFiles = dir.listFiles()
        if (listFiles.isNullOrEmpty()) {
            continue
        }
        for (child in listFiles) {
            if (!child.isHidden) {
                result += child.length()
                if (child.isDirectory) {
                    dirs.add(child)
                }
            }
        }
    }
    return result
}
