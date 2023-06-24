package com.kplc.outage.outage.data.remote.Dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartDto(
    @SerialName("areas")
    val areas: List<AreaDto>,
    @SerialName("part")
    val part: String
)