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


fun showDialogPermission(context: Activity, title_id: Int, contentId: Int) {
    val dialogPermission: AlertDialog?

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

    /*builder.setNegativeButton(
        "Exit"
    ) { _, _ ->
        dialogPermission?.dismiss()
    }*/

    dialogPermission = builder.create()

    if (!context.isFinishing) {
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
    onGrant: () -> Unit = {},
    onShowDialog: (() -> Unit)? = null,
) {
    this.doRequestPermission(permissions, {
        onGrant()
    }, {
        if (!requireActivity().isFinishing) {
            try {
                if (onShowDialog == null) {
                    showDialogPermission(
                        requireActivity(),
                        title_id,
                        contentId
                    )
                } else {
                    onShowDialog.invoke()
                }
            } catch (e: Exception) {

            }
        }
    })
}