package com.kplc.outage.outage.model

data class OutageInformationRegionUiState(
    val region: String,
    val areas: List<OutageInformationAreaUiState> = emptyList(),
)



