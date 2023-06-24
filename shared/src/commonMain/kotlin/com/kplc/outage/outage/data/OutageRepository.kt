package com.kplc.outage.outage.data

import com.kplc.outage.outage.data.remote.Dto.OutageResponse
import com.kplc.outage.outage.data.remote.OutageService

class OutageRepository(private val outageService: OutageService) {
    suspend fun loadOutages(): OutageResponse {
        return outageService.fetchOutages()
    }
}
