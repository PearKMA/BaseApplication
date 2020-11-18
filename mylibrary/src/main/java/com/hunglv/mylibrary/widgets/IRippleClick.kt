package com.hunglv.mylibrary.widgets

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat

interface IRippleClick {
    fun setBackground(context: Context, view: View, bg: Drawable?) {
        var background = bg
        if (background == null) {
            val outValue = TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                outValue,
                true
            )
            background = ContextCompat.getDrawable(context, outValue.resourceId)
        }

        try {
            view.background = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                RippleDrawable(
                    ColorStateList.valueOf(context.getColor(R.color.darker_gray)),
                    background,
                    null
                )
            } else {
                RippleDrawable(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            android.R.color.darker_gray
                        )
                    ), background, null
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}