package com.baseandroid.baselibrary.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.baseandroid.baselibrary.utils.ToastUtils.killToast

abstract class BaseFragment<BD : ViewDataBinding> : Fragment() {
    // region Const and Fields
    protected lateinit var binding: BD
    // endregion

    // region override function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFullScreen()) {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }
        if (isClearFullScreen()) {
            @Suppress("DEPRECATION")
            activity?.window?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    insetsController?.show(WindowInsets.Type.statusBars())
                    setDecorFitsSystemWindows(false)
                } else {
                    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        decorView.systemUiVisibility =
                            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                    }
                }
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(getStatusBarColor(), isDarkTheme())

        initBackPress()
        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onPause() {
        killToast()
        initPause()
        super.onPause()
    }
    // endregion

    // region private function
    private fun initBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPressed()
                }
            })
    }

    @Suppress("DEPRECATION")
    private fun setStatusBarColor(statusBarColor: Int = Color.BLACK, darkTheme: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            activity?.window?.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                var newUIVisibility = decorView.systemUiVisibility
                newUIVisibility = if (darkTheme) {
                    newUIVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                } else {
                    newUIVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }

                decorView.systemUiVisibility = newUIVisibility
                this.statusBarColor = statusBarColor
            }
        }
    }

    // endregion

    // region open function
    open fun initViews() {

    }

    open fun handleBackPressed() {

    }

    open fun isFullScreen(): Boolean = false

    open fun isClearFullScreen(): Boolean = false

    open fun initPause() {

    }
    // endregion

    // region abstract function
    abstract fun getLayoutId(): Int

    abstract fun isDarkTheme(): Boolean

    abstract fun getStatusBarColor(): Int
    // endregion

    // region safe navigation
    protected fun onNavigateUp(viewId: Int) {
        if (findNavController().currentDestination?.id == viewId) {
            findNavController().navigateUp()
        }
    }

    protected fun onNavigate(viewId: Int, deepLink: Int, bundle: Bundle? = null) {
        if (findNavController().currentDestination?.id == viewId) {
            if (bundle == null) {
                findNavController().navigate(deepLink)
            } else {
                findNavController().navigate(deepLink, bundle)
            }
        }
    }
    // endregion
}