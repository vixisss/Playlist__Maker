package com.example.playlist__maker.utils

sealed interface ResponseCode<T> {
    data class Success<T>(val data: T) : ResponseCode<T>
    data class ClientError<T>(val status: Int) : ResponseCode<T>
    data class ServerError<T>(val status: Int) : ResponseCode<T>
}