package com.baseandroid.baseapplication

import android.view.View
import androidx.databinding.BindingAdapter
import com.baseandroid.baselibrary.utils.extension.onDebounceClick
import com.baseandroid.baselibrary.widgets.AlphaTextView
import com.baseandroid.baselibrary.widgets.OnAlphaViewListener


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