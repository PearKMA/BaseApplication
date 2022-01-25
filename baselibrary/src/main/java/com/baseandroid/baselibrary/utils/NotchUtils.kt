package com.baseandroid.baselibrary.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.view.View
import com.baseandroid.baselibrary.utils.extension.buildVersion
import com.baseandroid.baselibrary.utils.extension.isBuildLargerThan
import java.lang.reflect.InvocationTargetException


object NotchUtils {
    private const val NOTCH_HUA_WEI = "com.huawei.android.util.HwNotchSizeUtil"
    private const val NOTCH_OPPO = "com.oppo.feature.screen.heteromorphism"
    private const val NOTCH_VIVO = "android.util.FtFeature"
    private const val NOTCH_XIAO_MI = "ro.miui.notch"
    private const val SYSTEM_PROPERTIES = "android.os.SystemProperties"

    fun Activity?.hasNotchScreen(): Boolean {
        return this != null && (hasNotchAtXiaoMi() ||
                hasNotchAtHuaWei() ||
                hasNotchAtOPPO() ||
                hasNotchAtVIVO() ||
                hasNotchAtAndroidP())
    }

    fun View?.hasNotchScreen(): Boolean {
        return this != null && (this.context.hasNotchAtXiaoMi() ||
                this.context.hasNotchAtHuaWei() ||
                this.context.hasNotchAtOPPO() ||
                this.context.hasNotchAtVIVO() ||
                hasNotchAtAndroidP())
    }

    private fun View?.hasNotchAtAndroidP(): Boolean {
        if (isBuildLargerThan(buildVersion.P)) {
            return this?.rootWindowInsets?.displayCutout != null
        }
        return false
    }

    private fun Activity?.hasNotchAtAndroidP(): Boolean {
        if (isBuildLargerThan(buildVersion.P)) {
            return this?.window?.decorView?.rootWindowInsets?.displayCutout != null
        }
        return false
    }

    @SuppressLint("PrivateApi")
    private fun Context?.hasNotchAtXiaoMi(): Boolean {
        var result = 0
        if ("Xiaomi" == Build.MANUFACTURER) {
            try {
                val classLoader = this?.classLoader
                val aClass = classLoader?.loadClass(SYSTEM_PROPERTIES)
                val method = aClass?.getMethod(
                    "getInt", String::class.java,
                    Int::class.javaPrimitiveType
                )
                result = method?.invoke(aClass, NOTCH_XIAO_MI, 0) as Int
            } catch (ignored: NoSuchMethodException) {

            } catch (ignored: IllegalAccessException) {

            } catch (ignored: InvocationTargetException) {

            } catch (ignored: ClassNotFoundException) {

            }
        }
        return result == 1
    }

    private fun Context?.hasNotchAtHuaWei(): Boolean {
        var result = false
        try {
            val classLoader = this?.classLoader
            val aClass = classLoader?.loadClass(NOTCH_HUA_WEI)
            val method = aClass?.getMethod("hasNotchInScreen")
            result = method?.invoke(aClass) as Boolean
        } catch (ignored: ClassNotFoundException) {

        } catch (ignored: NoSuchMethodException) {

        } catch (ignored: Exception) {

        }
        return result
    }

    @SuppressLint("PrivateApi")
    private fun Context?.hasNotchAtVIVO(): Boolean {
        var result = false
        try {
            val classLoader = this?.classLoader
            val aClass = classLoader?.loadClass(NOTCH_VIVO)
            val method = aClass?.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            result = method?.invoke(aClass, 0x00000020) as Boolean
        } catch (ignored: ClassNotFoundException) {

        } catch (ignored: NoSuchMethodException) {

        } catch (ignored: Exception) {

        }
        return result
    }

    private fun Context?.hasNotchAtOPPO(): Boolean {
        return try {
            this?.packageManager?.hasSystemFeature(NOTCH_OPPO) ?: false
        } catch (ignored: Exception) {
            false
        }
    }

    fun Activity?.getNotchHeight(): Int {
        var notchHeight = 0
        val rect = Rect()
        this?.window?.decorView?.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = if (rect.top > 0) rect.top else 24f.toPixel.toInt()

        if (isBuildLargerThan(buildVersion.P)) {
            val displayCutout = this?.window?.decorView?.rootWindowInsets?.displayCutout
            if (displayCutout != null) {
                notchHeight =
                    if (this?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        displayCutout.safeInsetTop
                    } else {
                        if (displayCutout.safeInsetLeft == 0) {
                            displayCutout.safeInsetRight
                        } else {
                            displayCutout.safeInsetLeft
                        }
                    }
            }

        } else {
            if (hasNotchAtXiaoMi()) {
                notchHeight = getXiaoMiNotchHeight()
            }

            if (hasNotchAtHuaWei()) {
                notchHeight = getHuaWeiNotchSize()[1]
            }
            if (hasNotchAtVIVO()) {
                notchHeight = 32f.toPixel.toInt()
                if (notchHeight < statusBarHeight) {
                    notchHeight = statusBarHeight
                }
            }
            if (hasNotchAtOPPO()) {
                notchHeight = 80
                if (notchHeight < statusBarHeight) {
                    notchHeight = statusBarHeight
                }
            }
        }
        return notchHeight
    }

    private fun Context?.getXiaoMiNotchHeight(): Int {
        val resourceId = this?.resources?.getIdentifier("notch_height", "dimen", "android") ?: 0
        return if (resourceId > 0) {
            this?.resources?.getDimensionPixelSize(resourceId) ?: 0
        } else {
            0
        }
    }

    private fun Context?.getHuaWeiNotchSize(): IntArray {
        val ret = intArrayOf(0, 0)
        return try {
            val cl = this?.classLoader
            val aClass = cl?.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = aClass?.getMethod("getNotchSize")
            get?.invoke(aClass) as IntArray
        } catch (ignored: ClassNotFoundException) {
            ret
        } catch (ignored: NoSuchMethodException) {
            ret
        } catch (ignored: java.lang.Exception) {
            ret
        }
    }
}