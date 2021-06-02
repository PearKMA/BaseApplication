package com.baseandroid.baselibrary.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.baseandroid.baselibrary.utils.ToastUtils.killToast
import com.baseandroid.baselibrary.utils.extension.isBuildLargerThan


enum class TypeScreen {
    FULL_SCREEN, NORMAL_SCREEN, TRANSLUCENT_STATUS_BAR, NONE
}

abstract class BaseFragment<BD : ViewDataBinding> : Fragment() {
    // region Const and Fields
    protected lateinit var binding: BD
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityReturned(result)
        }
    // endregion

    // region override function
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenType()
        if (!preventBackPress()) {
            initBackPress()
        }
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
    // endregion

    // region open function
    open fun initViews() {

    }

    open fun handleBackPressed() {

    }

    open fun onTypeScreen(): TypeScreen = TypeScreen.NONE

    open fun initPause() {

    }

    open fun preventBackPress(): Boolean = false

    open fun onActivityReturned(result: ActivityResult) {}
    // endregion

    // region abstract function
    abstract fun getLayoutId(): Int

    abstract fun isDarkTheme(): Boolean

    abstract fun getStatusBarColor(): Int
    // endregion

    // region protected method
    /**
     * @value TypeScreen#FULL_SCREEN: android 11 -> please set margin above navigation bar and below status bar
     * @value TypeScreen#TypeScreen.NORMAL_SCREEN: There's no need to set margin
     * @value TypeScreen#TRANSLUCENT_STATUS_BAR: similar FullScreen on Android 11
     * @value TypeScreen#NONE: default, no change settings from last config
     * */
    @Suppress("DEPRECATION")
    protected fun setScreenType() {
        when (onTypeScreen()) {
            TypeScreen.FULL_SCREEN -> {
                activity?.window?.apply {
                    if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                        setDecorFitsSystemWindows(false)
                        insetsController?.hide(WindowInsets.Type.statusBars())
                        insetsController?.show(WindowInsets.Type.navigationBars())
                        insetsController?.systemBarsBehavior =
                            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    } else {
                        addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_FULLSCREEN)
                    }
                    this.statusBarColor = this@BaseFragment.getStatusBarColor()
                }
            }
            TypeScreen.NORMAL_SCREEN -> {
                activity?.window?.apply {
                    if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                        setDecorFitsSystemWindows(true)
                        insetsController?.show(WindowInsets.Type.statusBars())
                        insetsController?.show(WindowInsets.Type.navigationBars())
                        insetsController?.systemBarsBehavior =
                            WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
                        if (isDarkTheme()) {
                            decorView.windowInsetsController?.setSystemBarsAppearance(
                                0, APPEARANCE_LIGHT_STATUS_BARS
                            )
                        } else {
                            decorView.windowInsetsController?.setSystemBarsAppearance(
                                APPEARANCE_LIGHT_STATUS_BARS,
                                APPEARANCE_LIGHT_STATUS_BARS
                            )
                        }
                        this.statusBarColor = this@BaseFragment.getStatusBarColor()
                    } else {
                        clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        decorView.systemUiVisibility = if (isDarkTheme()) {
                            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()) and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                        } else {
                            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()) or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        }
                        this.statusBarColor = this@BaseFragment.getStatusBarColor()
                    }

                }
            }
            TypeScreen.TRANSLUCENT_STATUS_BAR -> {
                if (isBuildLargerThan(Build.VERSION_CODES.M)) {
                    activity?.window?.apply {
                        if (isBuildLargerThan(Build.VERSION_CODES.R)) {
                            setDecorFitsSystemWindows(false)
                            insetsController?.show(WindowInsets.Type.statusBars())
                            insetsController?.show(WindowInsets.Type.navigationBars())
                            insetsController?.systemBarsBehavior =
                                WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
                            if (isDarkTheme()) {
                                decorView.windowInsetsController?.setSystemBarsAppearance(
                                    0, APPEARANCE_LIGHT_STATUS_BARS
                                )
                            } else {
                                decorView.windowInsetsController?.setSystemBarsAppearance(
                                    APPEARANCE_LIGHT_STATUS_BARS,
                                    APPEARANCE_LIGHT_STATUS_BARS
                                )
                            }
                            this.statusBarColor = this@BaseFragment.getStatusBarColor()
                        } else {
                            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                            decorView.systemUiVisibility = if (isDarkTheme()) {
                                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                            } else {
                                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            }
                            this.statusBarColor = this@BaseFragment.getStatusBarColor()
                        }
                    }

                }
            }
            TypeScreen.NONE -> {
            }
        }
    }

    protected fun onStartActivityForResult(intent: Intent, option: ActivityOptionsCompat? = null) {
        resultLauncher.launch(intent, option)
    }

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

    protected fun onNavigate(viewId: Int, action: NavDirections) {
        if (findNavController().currentDestination?.id == viewId) {
            findNavController().navigate(action)
        }
    }
    // endregion
}