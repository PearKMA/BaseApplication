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
import androidx.core.content.ContextCompat
import com.baseandroid.baselibrary.fragment.BaseFragment
import com.baseandroid.baselibrary.utils.extension.isBuildLargerThan

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

fun Context.checkPermissionsGranted(list: List<String>): Boolean {
    list.forEach { permission ->
        if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            if (!isBuildLargerThan(Build.VERSION_CODES.Q) && ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
    }
    return true
}

fun BaseFragment<*>.checkPermissions(
    permissions: List<String>,
    title_id: Int,
    contentId: Int,
    onGrant: () -> Unit = {}
) {
    this.doRequestPermission(permissions, {
        onGrant()
    }, { continueRequest ->
        if (!continueRequest) {
            PermissionUtils.showDialogPermission(
                requireActivity(),
                title_id,
                contentId
            )
        }
    })
}

/*fun checkFullPermission(
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
}*/