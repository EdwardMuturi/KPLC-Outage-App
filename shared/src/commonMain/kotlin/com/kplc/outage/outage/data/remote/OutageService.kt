package com.kplc.outage.outage.data.remote

import com.kplc.outage.outage.data.remote.Dto.OutageRequest
import com.kplc.outage.outage.data.remote.Dto.OutageResponse
import com.kplc.outage.outage.utils.OutageConstants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class OutageService(private val httpClient: HttpClient) {
    suspend fun fetchOutages(): OutageResponse {
        return httpClient.post {
            contentType(ContentType.Application.Json)
            setBody(OutageRequest(url = "https://www.kplc.co.ke/img/full/Interruptions - 26.01.2023.pdf"))
        }
            .bodyAsText()
            .let {
                Json.decodeFromString(it)
            }
    }
}
