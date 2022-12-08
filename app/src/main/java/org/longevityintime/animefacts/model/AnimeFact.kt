package org.longevityintime.animefacts.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeFact(
    @SerialName("fact_id") val id: Long,
    @SerialName("fact") val fact: String
)