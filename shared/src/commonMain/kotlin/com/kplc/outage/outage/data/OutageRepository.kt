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
        clearTables()
        saveRemoteData(outageResponse)
    }

    private fun clearTables() {
        database.areaQueries.deleteAll()
        database.partQueries.deleteAll()
        database.placeQueries.deleteAll()
        database.regionQueries.deleteAll()
    }

    private fun saveRemoteData(outageResponse: OutageResponse) {
        outageResponse.data.regions.onEachIndexed { index, regionDto ->
            saveRegion(regionDto)
            regionDto.parts.onEach { part ->
                savePart(part, regionDto)
                part.areas.onEach { area ->
                    saveArea(
                        area,
                        part,
                        regionDto.region,
                    )

                    area.places.onEach { place ->
                        savePlace(
                            regionDto.region,
                            place,
                            area,
                        )
                    }
                }
            }
        }
    }
    private fun savePlace(region: String, place: String, area: AreaDto) {
        database.placeQueries.insert(
            Place(
                place,
                area.area,
                region,
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
        region: String,
    ) {
        database.areaQueries.insert(Area(area.area, part.part, region))
    }

    private fun savePart(
        part: PartDto,
        it: RegionDto,
    ) {
        database.partQueries.insert(Part(part.part, it.region))
    }

    fun findAllRegions(): List<String> = database.regionQueries.selectAll().executeAsList()

    fun findAreaByRegion(regionId: String) =
        database.areaQueries.findByRegionId(regionId).executeAsList()

    fun findPlacesByArea(areaId: String) =
        database.placeQueries.findByAreaId(areaId).executeAsList()

    fun fetchPartsByRegionId(regionId: String) =
        database.partQueries.findByRegionId(regionId).executeAsList()

    fun findPlacesByRegion(regionId: String): List<Place> =
        database.placeQueries.findByRegionId(regionId).executeAsList()
}
