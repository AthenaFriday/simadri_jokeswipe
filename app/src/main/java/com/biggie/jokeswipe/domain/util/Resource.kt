package com.biggie.jokeswipe.domain.util

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()

    data class Error<out T>(val message: String, val data: T? = null) : Resource<T>()

    object Loading : Resource<Nothing>()
}