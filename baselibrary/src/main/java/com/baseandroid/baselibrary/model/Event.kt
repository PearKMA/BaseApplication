package com.baseandroid.baselibrary.model

import okhttp3.ResponseBody

sealed class EventStatus {
    data class Loading(var loading: Boolean, var progress: Int) : EventStatus()
    data class Success(var message: String) : EventStatus()
    data class Failure(var e: String) : EventStatus()

    companion object {
        fun loading(loading: Boolean, progress: Int): EventStatus = Loading(loading, progress)
        fun success(message: String = ""): EventStatus = Success(message)
        fun failure(error: String = ""): EventStatus = Failure(error)
    }
}


sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()

    object Loading : Resource<Nothing>()
}