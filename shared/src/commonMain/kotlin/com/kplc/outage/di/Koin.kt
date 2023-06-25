package com.kplc.outage.di

import com.kplc.outage.outage.presentation.OutageViewModel
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(isDebug: Boolean = false, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule(isDebug = isDebug), presentation, network, data)
    }

/**
 * Called by iOS etc
 */
fun KoinApplication.Companion.start(): KoinApplication = initKoin { }

val Koin.outageViewModel: OutageViewModel
    get() = get()
