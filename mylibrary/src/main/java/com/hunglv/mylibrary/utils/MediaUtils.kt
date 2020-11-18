package com.hunglv.mylibrary.utils

import android.media.MediaMetadataRetriever
import java.net.URLConnection

fun getTypeFromFile(path: String): String? {
    return try {
        //Lấy theo đuôi đường dẫn để tránh ký tự đặc biệt
        val ext = path.substring(path.lastIndexOf("."))
        URLConnection.guessContentTypeFromName(ext)
    } catch (e: Exception) {
        null
    }
}

fun isTypeNeedCheck(path: String, typeMedia: String): Boolean{
    //Nếu có lỗi là do có ký tự đặc biệt
    return try {
        val mimeType = getTypeFromFile(path)
        mimeType != null && mimeType.startsWith(typeMedia)
    } catch (e: java.lang.Exception) {
        false
    }
}

fun isMediaNotCorrupt(path: String, keyCode: Int): Boolean {
    val retriever = MediaMetadataRetriever()
    return try {
            retriever.setDataSource(path)
            val hasAudioStr =
                retriever.extractMetadata(keyCode) //MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO
            hasAudioStr != null && hasAudioStr == "yes"
        } catch (e: Exception) {
            false
        } finally {
            retriever.release()
        }

}

/**
 * Lấy thông tin độ dài file
 *
 * @param path đường dẫn tới file
 * @return độ dài của file theo mili giây
 */
fun getDurationFile(path: String): Long {
    val retriever = MediaMetadataRetriever()
    val timeInMillisecond: Long
    //use one of overloaded setDataSource() functions to set your data source
    timeInMillisecond = try {
        retriever.setDataSource(path)
        val time =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        time?.toLong() ?: 0
    } catch (e: Exception) {
        0
    } finally {
        retriever.release()
    }
    return timeInMillisecond
}