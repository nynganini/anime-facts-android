package org.longevityintime.animefacts.retrofit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.longevityintime.animefacts.model.Anime
import org.longevityintime.animefacts.model.AnimeFact

@Serializable
data class AnimeListWrapper(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val animeList: List<Anime>
)

@Serializable
data class AnimeFactsWrapper(
    @SerialName("success") val success: Boolean,
    @SerialName("total_facts") val totalFacts: Long,
    @SerialName("anime_img") val animeImageUrl: String,
    @SerialName("data") val animeFacts: List<AnimeFact>
)