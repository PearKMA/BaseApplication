package com.baseandroid.baselibrary.model

import okhttp3.ResponseBody

sealed class ActionEvent<out T> {
    object Loading : ActionEvent<Nothing>()
    data class Success<out T>(val value: T) : ActionEvent<T>()
    data class Failure(val throwable: Throwable?) : ActionEvent<Nothing>()
    object None : ActionEvent<Nothing>()
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