package com.baseandroid.baselibrary.utils

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.baseandroid.baselibrary.utils.extension.isBuildLargerThan
import java.io.File

@Suppress("DEPRECATION")
fun getRootPath(context: Context): String {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            getPrimaryStorageVolumeForAndroid11AndAbove(context)
        }
        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
            getPrimaryStorageVolumeAndroid10(context)
        }
        else -> {
            Environment.getExternalStorageDirectory().absolutePath
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun getPrimaryStorageVolumeForAndroid11AndAbove(context: Context): String {
    val myStorageManager =
        context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
    val mySV = myStorageManager.primaryStorageVolume
    return mySV.directory?.path ?: ""
}

@RequiresApi(Build.VERSION_CODES.N)
fun getPrimaryStorageVolumeAndroid10(context: Context): String {
    var volumeRootPath = ""
    val myStorageManager =
        context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
    val mySV = myStorageManager.primaryStorageVolume
    val storageVolumeClazz: Class<*>?
    try {
        storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
        val path = storageVolumeClazz.getMethod("getPath")
        volumeRootPath = path.invoke(mySV) as String
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return volumeRootPath
}

fun getPathFolder(context: Context, nameFolder: String): String {
    val path = "${getRootPath(context)}${File.separator}${nameFolder}"
    val folder = File(path)
    if (!folder.exists()) folder.mkdirs()
    return path
}

fun getOutputMediaDirectory(activity: Activity, name: String): File {
    val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
        File(it, name).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else activity.filesDir
}

fun getOutputFileDirectory(activity: Activity, name: String): File {
    return try {
        ContextCompat.getExternalFilesDirs(activity, null)[0]
    } catch (e: Exception) {
        activity.filesDir
    }
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
    return if (isBuildLargerThan(Build.VERSION_CODES.N)) FileProvider.getUriForFile(
        context,
        context.packageName.toString() + ".provider",
        File(path)
    ) else Uri.parse(
        "file://" + File(path).absolutePath
    )
}