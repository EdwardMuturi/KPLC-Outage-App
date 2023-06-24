package com.kplc.outage.outage.data

import com.kplc.outage.outage.data.local.sqldelight.AppDatabase
import com.kplc.outage.outage.data.local.sqldelight.sql.Area
import com.kplc.outage.outage.data.local.sqldelight.sql.Part
import com.kplc.outage.outage.data.local.sqldelight.sql.Place
import com.kplc.outage.outage.data.remote.Dto.AreaDto
import com.kplc.outage.outage.data.remote.Dto.OutageResponse
import com.kplc.outage.outage.data.remote.Dto.PartDto
import com.kplc.outage.outage.data.remote.Dto.RegionDto
import com.kplc.outage.outage.data.remote.OutageService

class OutageRepository(
    private val outageService: OutageService,
    private val database: AppDatabase,
) {
    suspend fun loadOutages() {
        val outageResponse = outageService.fetchOutages()
        saveRemoteData(outageResponse)
    }

    private fun saveRemoteData(outageResponse: OutageResponse) {
        outageResponse.data.regions.onEachIndexed { index, regionDto ->
            saveRegion(regionDto)
            savePart(regionDto.parts[index], regionDto)
            regionDto.parts[index].areas.onEach { area -> saveArea(area, regionDto.parts[index]) }
            regionDto.parts[index].areas.flatMap { it.places }.onEachIndexed { pIndex, place ->
                savePlace(
                    place,
                    regionDto.parts[index].areas[pIndex],
                )
            }
        }
    }
    private fun savePlace(place: String, area: AreaDto) {
        database.placeQueries.insert(
            Place(
                place,
                area.area,
                "${area.date.day / area.date.month / area.date.year} ${area.time.start}",
                "${area.date.day / area.date.month / area.date.year} ${area.time.end}",
            ),
        )
    }

    private fun saveRegion(it: RegionDto) {
        database.regionQueries.insert(it.region)
    }

    private fun saveArea(
        area: AreaDto,
        part: PartDto,
    ) {
        database.areaQueries.insert(Area(area.area, part.part))
    }

    private fun savePart(
        part: PartDto,
        it: RegionDto,
    ) {
        database.partQueries.insert(Part(part.part, it.region))
    }
}
