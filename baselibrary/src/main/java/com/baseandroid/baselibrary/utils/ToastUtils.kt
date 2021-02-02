package com.baseandroid.baselibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast

object ToastUtils {
    private var toast: Toast? = null

    /**
     * Hiện thông báo lên màn hình
     */
    @Suppress("DEPRECATION")
    @SuppressLint("ShowToast")
    fun showToast(context: Context, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        } else {
            when {
                toast == null -> {
                    // Create toast if found null, it would he the case of first call only
                    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                }
                toast!!.view == null -> {
                    // Toast not showing, so create new one
                    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                }
                else -> {
                    // Updating toast message is showing
                    toast!!.setText(message)
                }
            }
        }
        // Showing toast finally
        toast?.show()
    }

    fun checkToastNull() = toast == null

    /**
     * Xóa thông báo
     */
    fun killToast() {
        toast?.cancel()
        toast = null
    }
}