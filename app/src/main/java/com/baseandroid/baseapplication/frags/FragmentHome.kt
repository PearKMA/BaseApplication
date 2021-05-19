package com.baseandroid.baseapplication.frags

import android.graphics.Color
import com.baseandroid.baseapplication.R
import com.baseandroid.baseapplication.databinding.FragmentHomeBinding
import com.baseandroid.baselibrary.fragment.BaseFragment
import com.baseandroid.baselibrary.fragment.TypeScreen

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

    override fun isDarkTheme(): Boolean {
        return false
    }

    override fun getStatusBarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun initViews() {
        if (needReload) {
            needReload = false
            setScreenType()
        }
        binding.listener = this
    }

    override fun onTypeScreen(): TypeScreen {
        return TypeScreen.NON_FULL_SCREEN
    }

    override fun onNext() {
        needReload = true
        onNavigate(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentFullScreen)
    }

    // endregion

    // region private method

    // endregion
}