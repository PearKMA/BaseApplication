package com.baseandroid.baselibrary.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

@Suppress("DEPRECATION")
fun getRootPath(context: Context): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.getExternalFilesDir(null)!!.absolutePath
    } else {
        Environment.getExternalStorageDirectory().absolutePath
    }
}


fun getPathFolder(context: Context, nameFolder: String): String {
    val path = "${getRootPath(context)}${File.separator}${nameFolder}"
    val folder = File(path)
    if (!folder.exists()) folder.mkdirs()
    return path
}


/**
 * Lấy đường dẫn file từ uri
 *
 * @param context context
 * @param uri     uri của file
 * @return đường dẫn thực của file
 */
@Suppress("DEPRECATION")
fun getPathFromUri(context: Context, uri: Uri?): String? {
    var cursor: Cursor? = null
    return try {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } else {
            null
        }
    } catch (e: NullPointerException) {
        null
    } finally {
        cursor?.close()
    }
}

fun getUriFromPath(context: Context, path: String): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
        context,
        context.packageName.toString() + ".provider",
        File(path)
    ) else Uri.parse(
        "file://" + File(path).absolutePath
    )
}