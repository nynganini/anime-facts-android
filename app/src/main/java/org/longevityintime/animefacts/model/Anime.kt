package org.longevityintime.animefacts.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    @SerialName("anime_id") val id: Long,
    @SerialName("anime_name") val name: String,
    @SerialName("anime_img") val imageUrl: String
)