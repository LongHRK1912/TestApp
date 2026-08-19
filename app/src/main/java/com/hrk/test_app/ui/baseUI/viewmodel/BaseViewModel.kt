package com.hrk.test_app.ui.baseUI.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel<UiState, Event>(initialUiState: UiState) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    protected fun updateUiState(transform: (UiState) -> UiState) {
        _uiState.value = transform(_uiState.value)
    }

    open fun handleEvent(event: Event) {}

    protected fun launch(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) { block() }
    }

    protected fun <T> async(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend () -> T
    ) = viewModelScope.async(dispatcher) { block() }
}
