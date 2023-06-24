package com.kplc.outage.outage.data.remote.Dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataDto(
    @SerialName("regions")
    val regions: List<RegionDto>
)