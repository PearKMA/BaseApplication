package com.baseandroid.baselibrary.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseViewModel : ViewModel() {
    // region Const and Fields
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // handle thrown exceptions from coroutine scope
        onExceptionResults(throwable)
    }
    // endregion

    // region protected methods
    protected fun onExceptionResults(throwable: Throwable) {}
    // endregion
}