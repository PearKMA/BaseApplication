package com.baseandroid.baselibrary.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.baseandroid.baselibrary.utils.extension.isBuildLargerThan
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionUtils {
    fun showDialogPermission(context: Activity, title_id: Int, contentId: Int) {
        var dialogPermission: AlertDialog? = null

        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(title_id))
        builder.setMessage(context.getString(contentId))
        builder.setCancelable(true)
        builder.setPositiveButton(
            "Go to settings"
        ) { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(Uri.fromParts("package", context.packageName, null))
            context.startActivity(intent)
        }

        builder.setNegativeButton(
            "Exit"
        ) { _, _ ->
            dialogPermission!!.dismiss()
        }

        dialogPermission = builder.create()

        dialogPermission.show()
    }
}

fun checkFullPermission(
    activity: Activity,
    permissions: List<String>,
    title_id: Int,
    contentId: Int,
    onGrant: () -> Unit = {}
) {
    Dexter.withContext(activity)
        .withPermissions(permissions)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                if (!activity.isFinishing) {
                    if (p0?.areAllPermissionsGranted() == true) {
                        onGrant()
                    } else if (p0?.deniedPermissionResponses?.isEmpty() == false || p0?.isAnyPermissionPermanentlyDenied == true) {
                        if (isBuildLargerThan(Build.VERSION_CODES.Q)) {
                            if (p0.deniedPermissionResponses.size == 1 &&
                                p0.deniedPermissionResponses[0].permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) {
                                onGrant()
                            } else {
                                PermissionUtils.showDialogPermission(activity, title_id, contentId)
                            }
                        } else {
                            PermissionUtils.showDialogPermission(activity, title_id, contentId)
                        }
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }
        })
        .onSameThread()
        .check()
}


fun Context.checkPermissionsGranted(list: List<String>): Boolean {
    var result = true
    list.forEach { permission ->
        if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            if (!isBuildLargerThan(Build.VERSION_CODES.Q) && this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                result = false
            }
        } else {
            if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                result = false
            }
        }
    }
    return result
}