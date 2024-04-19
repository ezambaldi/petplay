package com.example.myapplicationtest.bases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<
        Intent : BaseMviViewModel.BaseViewIntent,
        State : BaseMviViewModel.BaseViewState,
        Effect : BaseMviViewModel.BaseViewEffect,
        > : ViewModel() {

    protected abstract fun inicialState(): State
    abstract fun intent(intent: Intent)
    private val initialState: State by lazy { inicialState() }
    val currentState: State get() = state.value
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _viewEffect = MutableSharedFlow<Effect>()
    val viewEffect = _viewEffect.asSharedFlow()

    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _state.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _viewEffect.emit(effectValue) }
    }

    interface BaseViewState
    interface BaseViewEffect
    interface BaseViewIntent

}