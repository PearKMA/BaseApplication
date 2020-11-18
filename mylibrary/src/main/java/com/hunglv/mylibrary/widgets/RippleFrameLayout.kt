package com.hunglv.mylibrary.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.hunglv.mylibrary.widgets.IRippleClick

class RippleFrameLayout : FrameLayout, IRippleClick {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setOnClickListener(l: OnClickListener?) {
        setBackground(context, this, background)
        super.setOnClickListener(l)
    }
}