package com.baseandroid.baselibrary.utils.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.MutableStateFlow

fun <T> LifecycleOwner.observer(liveData: LiveData<T>?, onDataChange: (T?) -> Unit) {
    liveData?.observe(this, Observer(onDataChange))
}

inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}

// require lifecycle runtime
//fun <T> LifecycleOwner.collectWhenStarted(flow: Flow<T>, firstTimeDelay: Long = 0L, action: suspend (value: T) -> Unit) {
//    lifecycleScope.launch {
//        delay(firstTimeDelay)
//        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//            flow.collect(action)
//        }
//    }
//}

inline fun <T> LifecycleOwner.singleObserver(
    liveData: LiveData<Event<T>>?,
    crossinline onDataChange: (T?) -> Unit
) {
    liveData?.observe(this, { it?.getContentIfNotHandled()?.let(onDataChange) })
}

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}


