package com.baseandroid.baselibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity <BD: ViewDataBinding> : AppCompatActivity() {
    // region Const and Fields
    protected lateinit var binding: BD
    // endregion

    // region override function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBeforeCreateViews()
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        if (isSingleTask()){
            if (!isTaskRoot){
                finish()
                return
            }
        }
        initViews()
        initEvents()
    }
    // endregion

    // region abstract function
    abstract fun getLayoutId(): Int
    // endregion

    // region protected function
    protected open fun initBeforeCreateViews() {

    }

    protected open fun initViews() {

    }


    protected open fun initEvents(){

    }
    // endregion

    // region open function
    open fun isSingleTask(): Boolean = false
    // endregion
}