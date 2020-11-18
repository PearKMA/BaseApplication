package com.hunglv.mylibrary.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class AlphaTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    interface OnAlphaTextListener {
        fun onClick()
    }

    private var onClickView: OnAlphaTextListener? = null

    fun setOnAlphaTextListener(onClickView: OnAlphaTextListener) {
        this.onClickView = onClickView
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (null != event) {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    alpha = 1f
                    onClickView?.onClick()
                }
                MotionEvent.ACTION_CANCEL -> {
                    alpha = 1f
                }
                else -> {
                    alpha = 0.5f
                }
            }
        }
        return true
    }
}