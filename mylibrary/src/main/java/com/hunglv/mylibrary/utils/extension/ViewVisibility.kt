package com.hunglv.mylibrary.utils.extension

import android.view.View

fun View.isVisible() = this.visibility == View.VISIBLE
fun View.isInVisible() = this.visibility == View.INVISIBLE
fun View.isGone() = this.visibility == View.GONE

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}