package com.baseandroid.mylibrary.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Định dạng thời gian từ mili giây sang phút:giây hoặc giờ:phút:giây
 *
 * @param timeInMilisec mili giây cần định dạng
 * @return thời gian đã được định dạng
 */
fun formatTime(timeInMilisec: Long): String {
    val hour = TimeUnit.MILLISECONDS.toHours(timeInMilisec)
    val mm = TimeUnit.MILLISECONDS.toMinutes(timeInMilisec) % 60
    val ss = TimeUnit.MILLISECONDS.toSeconds(timeInMilisec) % 60
    return if (hour == 0L) String.format(
        Locale.US,
        "%02d:%02d",
        mm,
        ss
    ) else String.format(Locale.US, "%02d:%02d:%02d", hour, mm, ss)
}

/**
 * Định dạng thời gian từ mili giây sang giờ:phút:giây đầy đủ
 *
 * @param timeInMilis mili giây cần định dạng
 * @return thời gian đã được định dạng
 */
fun formatFullTime(timeInMilis: Long): String {
    val hour = TimeUnit.MILLISECONDS.toHours(timeInMilis)
    val mm = TimeUnit.MILLISECONDS.toMinutes(timeInMilis) % 60
    val ss = TimeUnit.MILLISECONDS.toSeconds(timeInMilis) % 60
    return String.format(Locale.US, "%02d:%02d:%02d", hour, mm, ss)
}

fun getCurrentTime(): String {
    val currentTime = Calendar.getInstance().time
    val simpleDateFormat =
        SimpleDateFormat("HH:mm:ss", Locale.US)
    return simpleDateFormat.format(currentTime)
}

fun getCurrentDate(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy_HH/mm/ss")
        current.format(formatter).toString()
    } else {
        val date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy_HH/mm/ss", Locale.US)
        formatter.format(date).toString()
    }
}