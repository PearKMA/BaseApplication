package com.baseandroid.baselibrary.adapter

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
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

@BindingAdapter("marginTop")
fun View.setMarginTop(space: Float) {
    if (this.layoutParams is ConstraintLayout.LayoutParams) {
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelOffset(resourceId)
        }
        val actionBarSize = statusBarHeight + space
        params.setMargins(0, actionBarSize.toInt(), 0, 0)
    }
}

@BindingAdapter("marginBottom")
fun View.setMarginBottom(space: Float) {
    if (this.layoutParams is ConstraintLayout.LayoutParams) {
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier(
            "navigation_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelOffset(resourceId)
        }
        if (isBuildLargerThan(Build.VERSION_CODES.R) && statusBarHeight > 0) {
            val actionBarSize = statusBarHeight + space
            params.setMargins(0, 0, 0, actionBarSize.toInt())
        } else {
            params.setMargins(0, 0, 0, space.toInt())
        }
    }
}

@BindingAdapter("marginBottom")
fun ViewGroup.setMarginBottom(space: Float) {
    if (this.layoutParams is ConstraintLayout.LayoutParams) {
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier(
            "navigation_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelOffset(resourceId)
        }
        if (isBuildLargerThan(Build.VERSION_CODES.R) && statusBarHeight > 0) {
            val actionBarSize = statusBarHeight + space
            params.setMargins(0, 0, 0, actionBarSize.toInt())
        } else {
            params.setMargins(0, 0, 0, space.toInt())
        }
    }
}