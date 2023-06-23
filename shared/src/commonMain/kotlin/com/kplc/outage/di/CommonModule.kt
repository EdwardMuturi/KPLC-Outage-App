package com.kplc.outage.di

import com.kplc.outage.presentation.utils.Greeting
import com.kplc.outage.presentation.viewmodels.MainViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commonModule(isDebug: Boolean) = module {
    singleOf(::Greeting)

    singleOf(::MainViewModel)
}
