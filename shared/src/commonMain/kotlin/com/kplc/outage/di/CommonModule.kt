package com.kplc.outage.di

import com.kplc.outage.outage.data.OutageRepository
import com.kplc.outage.outage.data.local.sqldelight.createDatabase
import com.kplc.outage.outage.data.remote.OutageService
import com.kplc.outage.outage.domain.FetchOutagesUseCase
import com.kplc.outage.outage.presentation.OutageViewModel
import com.kplc.outage.outage.utils.OutageConstants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

fun commonModule(isDebug: Boolean) = module {
}

val presentation = module {
    single { OutageViewModel(get()) }
}

val network = module {
    single {
        HttpClient() {
            defaultRequest {
                url {
                    host = BASE_URL
                    protocol = URLProtocol.HTTPS
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 3000L
                connectTimeoutMillis = 3000L
                socketTimeoutMillis = 3000L
            }
        }
    }

    single { OutageService(get()) }
}

val data = module {
    single { createDatabase(get()) }
    single { OutageRepository(get(), get()) }
    single { FetchOutagesUseCase(get()) }
}

expect fun platformModule(): Module

