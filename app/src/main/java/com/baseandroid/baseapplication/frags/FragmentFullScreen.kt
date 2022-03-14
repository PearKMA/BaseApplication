package com.baseandroid.baseapplication.frags

import android.graphics.Color
import android.os.Bundle
import com.baseandroid.baseapplication.R
import com.baseandroid.baseapplication.databinding.FragmentFullScreenBinding
import com.baseandroid.baselibrary.fragment.BaseFragment
import com.baseandroid.baselibrary.fragment.TypeScreen

interface IFullScreenListener {
    fun onClose()
}

class FragmentFullScreen : BaseFragment<FragmentFullScreenBinding>(), IFullScreenListener {
    // region Const and Fields

    // endregion

    // region override/ listener method
    override fun getLayoutId(): Int {
        return R.layout.fragment_full_screen
    }

    override fun isDarkTheme(): Boolean {
        return false
    }

    override fun getStatusBarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun initViews(savedInstanceState: Bundle?) {
        binding.listener = this
    }

    override fun typeScreen(): TypeScreen {
        return TypeScreen.FULL_SCREEN
    }

    override fun handleBackPressed() {
        onClose()
    }

    override fun onClose() {
        onNavigateUp(R.id.fragmentFullScreen)
    }
    // endregion

    // region private method

    // endregion
}