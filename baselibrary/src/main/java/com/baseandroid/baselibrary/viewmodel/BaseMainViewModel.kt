package com.baseandroid.baselibrary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseMainViewModel<EventClass : Any>(application: Application) :
    AndroidViewModel(application) {
    // region Const and Fields
    protected val eventsChannel = Channel<EventClass>()
    val events = eventsChannel.receiveAsFlow()

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // handle thrown exceptions from coroutine scope
        onExceptionResults(throwable)
    }
    // endregion

    // region open methods
    open fun send(eventClass: EventClass, doActionBeforeSend: () -> Unit = {}) {
        viewModelScope.launch {
            doActionBeforeSend.invoke()
            eventsChannel.send(eventClass)
        }
    }
    // endregion

    // region protected methods
    protected fun onExceptionResults(throwable: Throwable) {}
    // endregion
}