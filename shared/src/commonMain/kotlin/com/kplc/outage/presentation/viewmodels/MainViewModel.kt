package com.kplc.outage.presentation.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MainViewModel : KoinComponent {

    // @NativeCoroutineScope
    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    private val _greeting = MutableStateFlow<String?>(null)
    val greeting get() = _greeting

    init {
        greetings()
    }

    private fun greetings() = viewModelScope.launch {
        _greeting.value = "Hello, World!"
    }
}
