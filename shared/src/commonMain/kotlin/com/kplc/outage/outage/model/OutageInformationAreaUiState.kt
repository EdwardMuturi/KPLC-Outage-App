package com.kplc.outage.outage.model

data class OutageInformationAreaUiState(
    val name: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val places: List<OutageInformationPlacesUiState> = emptyList(),
)
