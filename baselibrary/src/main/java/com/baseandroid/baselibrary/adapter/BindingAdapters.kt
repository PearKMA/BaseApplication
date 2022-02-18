package com.baseandroid.baselibrary.adapter

import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.baseandroid.baselibrary.utils.NotchUtils.getInternalDimensionSize
import com.baseandroid.baselibrary.utils.extension.NAVIGATION_BAR_HEIGHT
import com.baseandroid.baselibrary.utils.extension.buildVersion
import com.baseandroid.baselibrary.utils.extension.isBuildLargerThan
import com.baseandroid.baselibrary.utils.extension.onDebounceClick


private const val DEBOUNCE_CLICK_INTERVAL = 350L
private const val DEBOUNCE_LONG_CLICK_INTERVAL = 750L

@BindingAdapter("debounceClick")
fun View.onDebounceClick(listener: View.OnClickListener) {
    this.onDebounceClick(DEBOUNCE_CLICK_INTERVAL) {
        listener.onClick(this)
    }
}

@BindingAdapter("debounceLongClick")
fun View.onDebounceLongClick(listener: View.OnClickListener) {
    this.onDebounceClick(DEBOUNCE_LONG_CLICK_INTERVAL) {
        listener.onClick(this)
    }
}

@Suppress("DEPRECATION")
@BindingAdapter("paddingTop")
fun View.setPaddingTop(space: Float) {
    if (isBuildLargerThan(buildVersion.P)) {
        this.setOnApplyWindowInsetsListener { _, windowInsets ->
            val safeHeight = windowInsets.displayCutout?.safeInsetTop ?: 0
            val actionBarSize = safeHeight + space
            this.setPadding(0, actionBarSize.toInt(), 0, 0)
            if (this.layoutParams is ConstraintLayout.LayoutParams) {
                val params = this.layoutParams as ConstraintLayout.LayoutParams
                val tv = TypedValue()
                context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
                val actionBarHeight = context.resources.getDimensionPixelSize(tv.resourceId)
                params.height = actionBarHeight + actionBarSize.toInt()
            }
            windowInsets
        }
    }
}

@Suppress("DEPRECATION")
@BindingAdapter("marginTop")
fun View.setMarginTop(space: Float) {
    if (isBuildLargerThan(buildVersion.P)) {
        this.setOnApplyWindowInsetsListener { _, windowInsets ->
            val safeHeight = windowInsets.displayCutout?.safeInsetTop ?: 0
            val actionBarSize = safeHeight + space

            val params = this.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, actionBarSize.toInt(), 0, 0)

            /*if (isBuildLargerThan(buildVersion.R)) {
                WindowInsets.CONSUMED
            } else {
                windowInsets.consumeDisplayCutout()
            }*/
            windowInsets
        }
    }
    /*if (this.layoutParams is ConstraintLayout.LayoutParams) {
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        val statusBarHeight = this.context.getInternalDimensionSize(
            STATUS_BAR_HEIGHT
        )
        val actionBarSize = statusBarHeight + space
        params.setMargins(0, actionBarSize.toInt(), 0, 0)
    }*/
}

@BindingAdapter("marginBottom")
fun View.setMarginBottom(space: Float) {
    if (this.layoutParams is ConstraintLayout.LayoutParams) {
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        val navigationBarHeight = this.context.getInternalDimensionSize(
            NAVIGATION_BAR_HEIGHT
        )
        if (isBuildLargerThan(buildVersion.R) && navigationBarHeight > 0) {
            val actionBarSize = navigationBarHeight + space
            params.setMargins(0, 0, 0, actionBarSize.toInt())
        } else {
            params.setMargins(0, 0, 0, space.toInt())
        }
    }
}