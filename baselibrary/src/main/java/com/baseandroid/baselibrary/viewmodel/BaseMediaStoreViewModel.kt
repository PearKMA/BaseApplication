package com.baseandroid.baselibrary.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseMediaStoreViewModel(application: Application) : AndroidViewModel(application) {
    private var contentObserver: ContentObserver? = null

    open fun fetchData() {
        viewModelScope.launch {
            actionFetchData()
            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    getUriStore()
                ) {
                    fetchData()
                }
            }
        }
    }

    abstract suspend fun actionFetchData()

    open fun getUriStore(): Uri = MediaStore.Files.getContentUri("external")

    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }
}

/**
 * Convenience extension method to register a [ContentObserver] given a lambda.
 */
private fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}