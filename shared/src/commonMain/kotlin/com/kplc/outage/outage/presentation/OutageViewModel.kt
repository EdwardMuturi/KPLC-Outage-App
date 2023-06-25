package com.kplc.outage.outage.presentation

import com.kplc.outage.domain.utils.utils.toCommonMutableStateFlow
import com.kplc.outage.domain.utils.utils.toCommonStateFlow
import com.kplc.outage.outage.domain.FetchOutagesUseCase
import com.kplc.outage.outage.model.OutageInformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class OutageViewModel constructor(private val fetchOutagesUseCase: FetchOutagesUseCase) :
    KoinComponent {
    private val _outageInformationUiState: MutableStateFlow<OutageInformation> =
        MutableStateFlow(OutageInformation()).toCommonMutableStateFlow()
    val outageInformationUiState: StateFlow<OutageInformation> get() = _outageInformationUiState.toCommonStateFlow()
    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    fun fetchOutages() {
        viewModelScope.launch {
            fetchOutagesUseCase().collectLatest { state ->
                _outageInformationUiState.update { state }
            }
        }
    }
}
