package org.longevityintime.animefacts.retrofit

import org.longevityintime.animefacts.model.Anime
import org.longevityintime.animefacts.model.AnimeFact

interface NetworkDataSource {
    suspend fun getAnimeList(): NetworkResult<List<Anime>>
    suspend fun getAnimeFacts(name: String): NetworkResult<List<AnimeFact>>
}

sealed interface NetworkResult<out T> {
    data class Success<out T>(val value: T): NetworkResult<T>
    data class GenericError(val code: Int? = null, val error: String? = null): NetworkResult<Nothing>
    object NetworkError: NetworkResult<Nothing>
}