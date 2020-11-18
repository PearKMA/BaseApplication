package com.hunglv.baseapplication

import android.view.View
import androidx.databinding.BindingAdapter
import com.hunglv.mylibrary.utils.extension.onDebounceClick


@BindingAdapter("debounceClick")
fun View.onDebounceClick(listener: View.OnClickListener) {
    this.onDebounceClick {
        listener.onClick(this)
    }
}