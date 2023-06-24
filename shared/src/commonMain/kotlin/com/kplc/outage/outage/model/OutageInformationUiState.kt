package com.kplc.outage.outage.model

data class OutageInformationUiState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val regions: List<OutageInformationRegionUiState> = emptyList(),
)