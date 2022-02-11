package com.baseandroid.baselibrary.model

sealed class ActionEvent<out T> {
    object Loading : ActionEvent<Nothing>()
    data class Success<out T>(val value: T) : ActionEvent<T>()
    data class Failure(val throwable: Throwable?) : ActionEvent<Nothing>()
    object None : ActionEvent<Nothing>()
}


sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(
        val throwable: Throwable,
        val data: T? = null
    ) : Resource<Nothing>()

    object Loading : Resource<Nothing>()
}