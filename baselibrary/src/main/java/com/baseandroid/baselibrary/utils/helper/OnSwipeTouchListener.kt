package com.baseandroid.baselibrary.utils.helper

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.atan2

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
            // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
            // Let e1 be the initial event
            // e2 can be located at 4 different positions, consider the following diagram
            // (Assume that lines are separated by 90 degrees.)
            //
            //
            //         \ A  /
            //          \  /
            //       D   e1   B
            //          /  \
            //         / C  \
            //
            // So if (x2,y2) falls in region:
            //  A => it's an UP swipe
            //  B => it's a RIGHT swipe
            //  C => it's a DOWN swipe
            //  D => it's a LEFT swipe
            //
            val x1 = e1?.x ?: 0f
            val y1 = e1?.y ?: 0f

            val x2 = e2?.x ?: 0f
            val y2 = e2?.y ?: 0f

            val angle = getAngle(x1, y1, x2, y2)

            when (angle.toInt()) {
                in 45..135 -> onSwipeBottomToTop()
                in 0..45, in 315..360 -> onSwipeLeftToRight()
                in 225..315 -> onSwipeTopToBottom()
                else -> onSwipeRightToLeft()
            }

            return false

            /*var result = false
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

            return result*/
        }

        private fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Double {
            val rad = atan2(y1 - y2, x2 - x1) + PI
            return (rad * 180 / PI + 180) % 360
        }
    }
}
