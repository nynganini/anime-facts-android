@file:OptIn(ExperimentalSerializationApi::class)

package org.longevityintime.animefacts.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object DI {
    private const val BaseUrl = "https://www.nnbelectronics.com/anime-facts/api/"
    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }

    private val networkApi: RetrofitNetworkApi = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(json.asConverterFactory(contentType))
        .build().create(RetrofitNetworkApi::class.java)

    val networkDataSource = RetrofitNetwork(networkApi)
}