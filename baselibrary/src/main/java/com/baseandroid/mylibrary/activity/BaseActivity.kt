package com.baseandroid.mylibrary.activity

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
        if (isSingleTask()){
            if (!isTaskRoot){
                finish()
                return
            }
        }
        binding = DataBindingUtil.setContentView(this,getLayoutId())
        initViews()
        initEvents()
    }
    // endregion

    // region abstract function
    abstract fun getLayoutId(): Int
    // endregion

    // region protected function
    protected open fun initViews() {

    }


    protected open fun initEvents(){

    }
    // endregion

    // region open function
    open fun isSingleTask(): Boolean = false
    // endregion
}