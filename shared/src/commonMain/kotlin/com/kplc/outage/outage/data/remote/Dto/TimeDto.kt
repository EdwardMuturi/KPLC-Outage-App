package com.kplc.outage.outage.data.remote.Dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeDto(
    @SerialName("end")
    val end: String,
    @SerialName("start")
    val start: String
)