package com.example.whatuneedtodo.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Action>(initialState: State) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    abstract suspend fun handleAction(action: Action)

    fun catchAction(a: Action) {
        viewModelScope.launch { handleAction(a) }
    }

    protected fun updateState(morph: (State) -> State) {
        _state.update { morph(it) }
    }
}