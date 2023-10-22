package com.example.myapplicationtest.bases

sealed class GenericResult<T> {
    data class Success<T>(val data: T) : GenericResult<T>()
    data class Error<T>(
        val errorMessage: String,
        val errorTitle: String = "",
    ) : GenericResult<T>()
}
