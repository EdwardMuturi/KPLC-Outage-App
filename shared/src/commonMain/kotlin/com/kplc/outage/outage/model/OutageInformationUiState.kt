package com.kplc.outage.outage.model

data class OutageInformationUiState(
    val regions: List<OutageInformationRegionUiState> = emptyList(),
)