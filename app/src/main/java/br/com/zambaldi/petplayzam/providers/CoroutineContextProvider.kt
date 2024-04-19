package br.com.zambaldi.petplayzam.providers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineContextProvider {
    val main: CoroutineDispatcher
        get() = Dispatchers.Main

    val io: CoroutineDispatcher
        get() = Dispatchers.IO

    class Default : CoroutineContextProvider
}