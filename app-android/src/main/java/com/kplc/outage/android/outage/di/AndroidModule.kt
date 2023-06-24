package com.kplc.outage.android.outage.di

import com.kplc.outage.outage.data.local.sqldelight.DriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
    single { DriverFactory(androidContext()) }
}
