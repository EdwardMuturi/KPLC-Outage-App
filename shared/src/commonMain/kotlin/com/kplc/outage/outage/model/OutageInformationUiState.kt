package com.kplc.outage.outage.model

import kotlinx.serialization.Serializable

@Serializable
data class OutageInformationUiState(
    val region: String = "",
    val part: String = "",
    val area: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val places: List<String> = emptyList(),
)

data class OutageInformation(
    val message: String? = null,
    val isLoading: Boolean = false,
    val outages: List<OutageInformationUiState> = emptyList(),
)
