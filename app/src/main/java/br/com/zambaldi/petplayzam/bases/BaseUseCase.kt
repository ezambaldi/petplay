package com.example.myapplicationtest.bases

interface BaseUseCase<T> {
    suspend operator fun invoke(): GenericResult<T>
}