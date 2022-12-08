package org.longevityintime.animefacts.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeFact(
    val id: Long,
    val fact: String
)