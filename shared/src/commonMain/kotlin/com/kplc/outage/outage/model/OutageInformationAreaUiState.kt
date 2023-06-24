package com.kplc.outage.outage.model

data class OutageInformationAreaUiState(
    val name: String,
    val date: String,
    val time: String,
    val places: List<OutageInformationPlacesUiState>,
)