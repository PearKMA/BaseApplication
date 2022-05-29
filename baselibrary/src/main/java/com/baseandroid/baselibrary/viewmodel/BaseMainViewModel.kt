package com.baseandroid.baselibrary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseMainViewModel(application: Application) :
    AndroidViewModel(application) {
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