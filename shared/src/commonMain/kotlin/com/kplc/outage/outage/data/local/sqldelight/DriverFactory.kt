package com.kplc.outage.outage.data.local.sqldelight

import com.squareup.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory) = AppDatabase(driverFactory.createDriver())
