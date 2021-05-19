package com.baseandroid.baseapplication

import androidx.appcompat.app.AppCompatDelegate
import com.baseandroid.baseapplication.databinding.ActivityMainBinding
import com.baseandroid.baselibrary.activity.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(), IMainActivityListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.listener = this
    }

    override fun onTestClick() {
        //binding.tvTest.text = getString(R.string.app_name)
    }

    override fun onAlphaTestClick() {
        //binding.tvTest.text = getString(R.string.app_name)
    }

    override fun isSingleTask(): Boolean {
        return true
    }
}