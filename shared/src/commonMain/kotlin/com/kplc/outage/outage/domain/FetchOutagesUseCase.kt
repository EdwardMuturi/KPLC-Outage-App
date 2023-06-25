package com.kplc.outage.outage.domain

import com.kplc.outage.outage.data.OutageRepository
import com.kplc.outage.outage.model.OutageInformation
import com.kplc.outage.outage.model.OutageInformationUiState
import kotlinx.coroutines.flow.flow

class FetchOutagesUseCase(private val outageRepository: OutageRepository) {
    operator fun invoke() = flow {
        outageRepository.loadOutages()
        emit(OutageInformation(isLoading = true))
        val outages = mutableListOf<OutageInformationUiState>()
        outageRepository.findAllRegions()
            .onEach { region ->
                outageRepository.fetchPartsByRegionId(region)
                    .map { part ->
                        outageRepository.findAreaByRegion(region)
                            .map { area ->
                                outages.add(
                                    OutageInformationUiState(
                                        region = region.trim().trimIndent(),
                                        part = part.name,
                                        date = area.date,
                                        startTime = area.startTime,
                                        endTime = area.endTime,
                                        area = area.name,
                                        places = outageRepository.findPlacesByArea(area.name)
                                            .map { place -> place.name },
                                    ),
                                )
                            }
                    }
            }

        emit(OutageInformation(isLoading = false, outages = outages))
    }
}
