package com.kplc.outage.outage.data.remote.Dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaDto(
    @SerialName("area")
    val area: String,
    @SerialName("date")
    val date: DateDto,
    @SerialName("places")
    val places: List<String>,
    @SerialName("time")
    val time: TimeDto
)