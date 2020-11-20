package com.hunglv.baseapplication

import android.view.View
import androidx.databinding.BindingAdapter
import com.hunglv.mylibrary.utils.extension.onDebounceClick
import com.hunglv.mylibrary.widgets.AlphaTextView
import com.hunglv.mylibrary.widgets.OnAlphaViewListener


@BindingAdapter("debounceClick")
fun View.onDebounceClick(listener: View.OnClickListener) {
    this.onDebounceClick {
        listener.onClick(this)
    }
}

@BindingAdapter("alphaTextDebounceClick")
fun AlphaTextView.onAlphaTextDebounceClick(listener: OnAlphaViewListener) {
    this.onDebounceClick {
        listener.onClick(this)
    }
}