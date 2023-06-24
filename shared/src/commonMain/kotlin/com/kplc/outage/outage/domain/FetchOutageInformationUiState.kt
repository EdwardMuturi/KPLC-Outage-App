package com.kplc.outage.outage.domain

import com.kplc.outage.outage.data.OutageRepository
import com.kplc.outage.outage.data.local.sqldelight.sql.Place
import kotlinx.coroutines.flow.flow

class FetchOutageInformationUiState(private val outageRepository: OutageRepository) {
    operator fun invoke() = flow {
        val outages = outageRepository.findAllRegions()
            .mapToOutageInformationRegionUiStates { outageRepository.findPlacesByRegion(it) }
        emit(outages)
    }

    private fun List<String>.mapToOutageInformationRegionUiStates(fetchPlacesByRegion: (String) -> List<Place>) =
        this.map { region ->
            val places = fetchPlacesByRegion(region)
            OutageInformationRegionUiState(
                region,
                areas = places.map {
                    OutageInformationAreaUiState(
                        it.name,
                        it.startDate.plus(it.endDate),
                        it.endDate,
                        places.map { place -> OutageInformationPlacesUiState(place.name) },
                    )
                },
            )
        }
}

data class OutageInformationUiState(
    val regions: List<OutageInformationRegionUiState> = emptyList(),
)

data class OutageInformationRegionUiState(
    val region: String,
    val areas: List<OutageInformationAreaUiState> = emptyList(),
)

data class OutageInformationAreaUiState(
    val name: String,
    val date: String,
    val time: String,
    val places: List<OutageInformationPlacesUiState>,
)

data class OutageInformationPlacesUiState(val name: String)
