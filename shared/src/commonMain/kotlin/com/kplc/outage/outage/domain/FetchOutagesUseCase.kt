package com.kplc.outage.outage.domain

import com.kplc.outage.outage.data.OutageRepository
import com.kplc.outage.outage.data.local.sqldelight.sql.Place
import com.kplc.outage.outage.model.OutageInformationAreaUiState
import com.kplc.outage.outage.model.OutageInformationPlacesUiState
import com.kplc.outage.outage.model.OutageInformationRegionUiState
import com.kplc.outage.outage.model.OutageInformationUiState
import kotlinx.coroutines.flow.flow

class FetchOutagesUseCase(private val outageRepository: OutageRepository) {
    operator fun invoke() = flow {
        outageRepository.loadOutages()
        emit(OutageInformationUiState(isLoading = true))
        val outages = outageRepository.findAllRegions()
            .mapToOutageInformationRegionUiState { outageRepository.findPlacesByRegion(it) }
        emit(OutageInformationUiState(isLoading = false))
        emit(outages)
    }

    private fun List<String>.mapToOutageInformationRegionUiState(fetchPlacesByRegion: (String) -> List<Place>) =
        OutageInformationUiState(
            regions = this.map { region ->
                val places = fetchPlacesByRegion(region)
                OutageInformationRegionUiState(
                    region.trim().trimIndent(),
                    areas = places.map {
                        OutageInformationAreaUiState(
                            it.name,
                            "",
                            "",
                            places.map { place -> OutageInformationPlacesUiState(place.name) },
                        )
                    },
                )
            },
        )
}
