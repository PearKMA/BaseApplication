package com.baseandroid.baselibrary.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseMediaStoreViewModel(application: Application) :
    BaseMainViewModel(application) {
    //region Const and Fields
    private var contentObserver: ContentObserver? = null
    //endregion

    //region abstract methods
    abstract suspend fun actionFetchData(isStorageChange: Boolean = false)
    //endregion

    //region open methods
    open fun fetchData(isStorageChange: Boolean = false) {
        viewModelScope.launch {
            actionFetchData(isStorageChange)
            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    getUriStore()
                ) {
                    fetchData(true)
                }
            }
        }
    }

    open fun getUriStore(): Uri = MediaStore.Files.getContentUri("external")
    //endregion

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