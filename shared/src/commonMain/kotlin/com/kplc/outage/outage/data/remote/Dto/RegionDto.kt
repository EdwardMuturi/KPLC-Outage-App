package com.kplc.outage.outage.data.remote.Dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionDto(
    @SerialName("parts")
    val parts: List<PartDto>,
    @SerialName("region")
    val region: String
)