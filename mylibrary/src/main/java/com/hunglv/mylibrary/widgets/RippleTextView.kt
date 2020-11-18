package com.hunglv.mylibrary.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.hunglv.mylibrary.widgets.IRippleClick

class RippleTextView : AppCompatTextView, IRippleClick {
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