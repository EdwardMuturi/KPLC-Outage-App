package com.kplc.outage.outage.data.remote.Dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OutageResponse(
    @SerialName("data")
    val `data`: DataDto,
    @SerialName("message")
    val message: String,
    @SerialName("success")
    val success: Boolean
)