package com.baseandroid.baseapplication

import com.baseandroid.baseapplication.databinding.ActivityMainBinding
import com.baseandroid.mylibrary.activity.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(), IMainActivityListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        binding.listener = this
    }

    override fun onTestClick() {
        binding.tvTest.text = getString(R.string.app_name)
    }

    override fun onAlphaTestClick() {
        binding.tvTest.text = getString(R.string.app_name)
    }
}