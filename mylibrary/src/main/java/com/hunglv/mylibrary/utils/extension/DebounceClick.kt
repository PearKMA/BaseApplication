package com.hunglv.mylibrary.utils.extension

import android.os.SystemClock
import android.view.View

private const val DEFAULT_DEBOUNCE_INTERVAL = 500L

abstract class DebounceClickListener(
    private val maxTime: Long = DEFAULT_DEBOUNCE_INTERVAL
) : View.OnClickListener {
    // region Const and Fields
    private var mLastClickTime = 0L
    // endregion

    // region override function
    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < maxTime)
            return
        mLastClickTime = SystemClock.elapsedRealtime()
        onDebounceClick(v)
    }
    // endregion

    // region abstract function
    abstract fun onDebounceClick(v: View?)
    // endregion
}


fun View.onDebounceClick(
    maxTime: Long = DEFAULT_DEBOUNCE_INTERVAL,
    onClick: (view: View?) -> Unit
) {
    setOnClickListener(object : DebounceClickListener(maxTime) {
        override fun onDebounceClick(v: View?) = onClick(v)
    })
}