package com.kplc.outage.di

import com.kplc.outage.outage.data.local.sqldelight.DriverFactory
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DriverFactory() }
    single { Darwin.create() }
}
