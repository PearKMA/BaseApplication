package com.baseandroid.baselibrary.utils.helper

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class OnSwipeTouchListener(context: Context) : View.OnTouchListener {
    private val gestureDetector = GestureDetector(context, GestureListener())

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    open fun onSwipeLeftToRight() {}
    open fun onSwipeRightToLeft() {}
    open fun onSwipeBottomToTop() {}
    open fun onSwipeTopToBottom() {}
    open fun onSingleTap() {}

    companion object {
        private const val SWIPE_THRESHOLD = 60
        private const val SWIPE_VELOCITY_THRESHOLD = 60
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            if (null != e) {
                onSingleTap()
            }
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                if (null != e1 && null != e2) {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeLeftToRight()
                        } else {
                            onSwipeRightToLeft()
                        }
                        result = true
                    } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeTopToBottom()
                        } else {
                            onSwipeBottomToTop()
                        }
                        result = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }
    }
}