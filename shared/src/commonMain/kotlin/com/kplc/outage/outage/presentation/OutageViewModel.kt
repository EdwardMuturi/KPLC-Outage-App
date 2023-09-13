package com.kplc.outage.outage.presentation

import com.kplc.outage.domain.utils.utils.toCommonMutableStateFlow
import com.kplc.outage.domain.utils.utils.toCommonStateFlow
import com.kplc.outage.outage.domain.FetchOutagesUseCase
import com.kplc.outage.outage.model.OutageInformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class OutageViewModel constructor(private val fetchOutagesUseCase: FetchOutagesUseCase) :
    KoinComponent {
    private val _searchValue = MutableStateFlow("")
    val searchValue get() = _searchValue.asStateFlow()
    fun setSearchValue(value: String) {
        _searchValue.value = value
    }

    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    private val _outageInformationUiState: MutableStateFlow<OutageInformation> =
        MutableStateFlow(OutageInformation()).toCommonMutableStateFlow()
    val outageInformationUiState get() = _outageInformationUiState.asStateFlow().toCommonStateFlow()

    private var searchJob: Job? = null

    fun fetchOutages(searchString: String? = null) {
//        searchJob?.cancel()
        searchJob = viewModelScope.launch {
//            delay(500L)
            fetchOutagesUseCase(searchString).collectLatest { state ->
                _outageInformationUiState.update {
                    it.copy(
                        isLoading = state.isLoading,
                        message = state.message,
                        outages = state.outages.filter { outage ->
                            outage.region.contains(searchString ?: "", ignoreCase = true) ||
                                outage.part.contains(searchString ?: "", ignoreCase = true) ||
                                outage.area.contains(searchString ?: "", ignoreCase = true) ||
                                outage.date.contains(searchString ?: "", ignoreCase = true) ||
                                outage.startTime.contains(
                                    searchString ?: "",
                                    ignoreCase = true,
                                ) ||
                                outage.endTime.contains(
                                    searchString ?: "",
                                    ignoreCase = true,
                                ) ||
                                outage.places.any { place ->
                                    place.contains(searchString ?: "", ignoreCase = true)
                                }
                        },
                    )
                }
            }
        }
    }
}
