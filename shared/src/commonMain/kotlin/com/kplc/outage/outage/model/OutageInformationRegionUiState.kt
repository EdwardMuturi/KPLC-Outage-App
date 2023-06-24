package com.kplc.outage.outage.model

data class OutageInformationRegionUiState(
    /**
     * //Update values to
     * region
     * parts
     *
     * part
     *   name?, areas
     *
     *   area
     *     name, date, startTime, endTime, places
     *
     *     place
     *        name
     * */
    val region: String,
    val areas: List<OutageInformationAreaUiState> = emptyList(),
)



