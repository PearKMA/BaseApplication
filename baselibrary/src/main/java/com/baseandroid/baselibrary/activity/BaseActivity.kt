package com.baseandroid.baselibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<BD : ViewDataBinding> : AppCompatActivity() {
    // region Const and Fields
    protected lateinit var binding: BD
    // endregion

    // region override function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBeforeCreateViews(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        if (isSingleTask()) {
            if (!isTaskRoot) {
                finish()
                return
            }
        }
        if (!enableDarkMode()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initViews(savedInstanceState)
    }
    // endregion

    // region abstract function
    abstract fun getLayoutId(): Int
    // endregion

    // region protected function
    protected open fun initBeforeCreateViews(savedInstanceState: Bundle?) {

    }

    protected open fun initViews(savedInstanceState: Bundle?) {

    }
    // endregion

    // region open function
    open fun isSingleTask(): Boolean = false

    open fun enableDarkMode(): Boolean = false
    // endregion
}