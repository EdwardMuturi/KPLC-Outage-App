package com.kplc.outage.outage.model

data class Place(
    val Id: Int,
    val areaId: Int,
    val name: String,
    val startDate: String,
    val endDate: String
)
