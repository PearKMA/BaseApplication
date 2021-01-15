package com.baseandroid.mylibrary.utils.extension

import android.os.SystemClock
import android.view.View
import com.baseandroid.mylibrary.widgets.*

private const val DEFAULT_DEBOUNCE_INTERVAL = 500L

private var mLastClickTime = 0L

abstract class DebounceClickListener(
    private val maxTime: Long = DEFAULT_DEBOUNCE_INTERVAL
) : View.OnClickListener {

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

abstract class AlphaDebounceClickListener(
    private val maxTime: Long = DEFAULT_DEBOUNCE_INTERVAL
) : OnAlphaViewListener {

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
    when (this) {
        is AlphaTextView -> {
            setOnAlphaTextListener(object : AlphaDebounceClickListener(maxTime) {
                override fun onDebounceClick(v: View?) = onClick(v)
            })
        }
        is AlphaFrameLayout -> {
            setOnAlphaLayoutListener(object : AlphaDebounceClickListener(maxTime) {
                override fun onDebounceClick(v: View?) = onClick(v)
            })
        }
        is AlphaImageView -> {
            setOnAlphaImageListener(object : AlphaDebounceClickListener(maxTime) {
                override fun onDebounceClick(v: View?) = onClick(v)
            })
        }
        is AlphaLinearLayout -> {
            setOnAlphaLayoutListener(object : AlphaDebounceClickListener(maxTime) {
                override fun onDebounceClick(v: View?) = onClick(v)
            })
        }
        else -> {
            setOnClickListener(object : DebounceClickListener(maxTime) {
                override fun onDebounceClick(v: View?) = onClick(v)
            })
        }
    }
}

