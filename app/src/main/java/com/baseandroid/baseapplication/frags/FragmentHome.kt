package com.baseandroid.baseapplication.frags

import android.Manifest
import android.animation.FloatEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.animation.AccelerateDecelerateInterpolator
import com.baseandroid.baseapplication.R
import com.baseandroid.baseapplication.databinding.FragmentHomeBinding
import com.baseandroid.baselibrary.fragment.BaseFragment
import com.baseandroid.baselibrary.fragment.TypeScreen
import com.baseandroid.baselibrary.utils.checkPermissions

interface HomeListener {
    fun onNext()
}

class FragmentHome : BaseFragment<FragmentHomeBinding>(), HomeListener {
    // region Const and Fields
    private var needReload = false
    // endregion

    // region override/ listener method
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getViewId() = R.id.fragmentHome

    override fun isDarkTheme(): Boolean {
        return needReload
    }

    override fun getStatusBarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun typeScreen(): TypeScreen {
        return if (needReload) TypeScreen.TRANSLUCENT_STATUS_BAR else TypeScreen.NORMAL_SCREEN
    }

    private var i = 0
    override fun onNext() {
        needReload = true

        checkPermissions(
            listPermissions,
            R.string.text_title,
            R.string.text_content
        ) {
            onNavigate(R.id.action_fragmentHome_to_fragmentFullScreen)
        }
    }

    override fun handleBackPressed() {
        activity?.finish()
    }
    // endregion

    // region private method
    private val listPermissions =
        mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    private val valueAnimator = ValueAnimator()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private fun drawCanvas() {
        val d = GradientDrawable()
        d.shape = GradientDrawable.OVAL
        d.setSize(500, 500)
        d.setColor(Color.parseColor("#800085FF"))
        d.setStroke(0, Color.TRANSPARENT)

        val bitmap =
            Bitmap.createBitmap(d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // draw radial
        val radialGradient = RadialGradient(
            250f,
            250f,
            250f,
            Color.parseColor("#2962FF"),
            Color.parseColor("#232962FF"),
            Shader.TileMode.CLAMP
        )
        mPaint.shader = radialGradient

        canvas.drawArc(0f, 0f, 500f, 500f, 247.5f, 45f, true, mPaint)

        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)
        binding.ivCanvas.setImageBitmap(bitmap)

        val radius = 150
        val radiusHolder = PropertyValuesHolder.ofFloat("radius", 0f, radius.toFloat())
        val transparencyHolder = PropertyValuesHolder.ofFloat("transparency", 0f, 1f)

        if (valueAnimator.isRunning) {
            valueAnimator.removeAllListeners()
            valueAnimator.cancel()
        }

        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.RESTART
        valueAnimator.setValues(radiusHolder, transparencyHolder)
        valueAnimator.duration = 3000L
        valueAnimator.setEvaluator(FloatEvaluator())
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener {
            val animateAlpha = valueAnimator.getAnimatedValue("transparency") as Float
            binding.ivCanvas.alpha = animateAlpha
        }
        valueAnimator.start()
    }
    // endregion
}