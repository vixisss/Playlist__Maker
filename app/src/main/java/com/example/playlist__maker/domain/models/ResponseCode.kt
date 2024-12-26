package com.example.playlist__maker.domain.models

sealed interface ResponseCode<T> {
    data class Success<T>(val data: T) : ResponseCode<T>
    data class Error<T>(val message: String) : ResponseCode<T>
}