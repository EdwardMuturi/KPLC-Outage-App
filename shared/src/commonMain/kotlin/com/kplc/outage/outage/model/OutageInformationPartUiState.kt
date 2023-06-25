package com.kplc.outage.outage.model

data class OutageInformationPartUiState(
    val name: String = String(),
    val areas: List<OutageInformationAreaUiState> = emptyList()
)
