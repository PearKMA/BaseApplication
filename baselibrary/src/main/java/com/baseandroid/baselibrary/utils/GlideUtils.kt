package com.baseandroid.baselibrary.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun ImageView.loadNormal(
    source: Any,
    placeholder: Int = android.R.color.transparent,
    error: Int = android.R.color.transparent
) {
    Glide.with(this)
        .load(source)
        .placeholder(placeholder)
        .error(error)
        .into(this)
}

fun ImageView.loadCenterCrop(
    source: Any,
    placeholder: Int = android.R.color.transparent,
    error: Int = android.R.color.transparent
) {
    Glide.with(this)
        .load(source)
        .centerCrop()
        .placeholder(placeholder)
        .error(error)
        .into(this)
}

fun ImageView.loadRoundedCorner(
    source: Any, radius: Int,
    placeholder: Int = android.R.color.transparent,
    error: Int = android.R.color.transparent
) {
    Glide.with(this)
        .load(source)
        .transform(CenterCrop(), RoundedCorners(radius))
        .placeholder(placeholder)
        .error(error)
        .into(this)
}

fun View.loadBackground(source: Any) {
    Glide.with(this)
        .asDrawable()
        .load(source)
        .into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                this@loadBackground.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
}
