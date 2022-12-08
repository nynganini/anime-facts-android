package org.longevityintime.animefacts.retrofit

import android.util.Log
import org.longevityintime.animefacts.model.Anime
import org.longevityintime.animefacts.model.AnimeFact
import retrofit2.HttpException
import retrofit2.http.GET
import java.io.IOException

interface RetrofitNetworkApi {
    @GET("/")
    suspend fun getAnimeList(): AnimeListWrapper
    @GET("/{animeName}")
    suspend fun getAnimeFacts(name: String): AnimeFactsWrapper
}

class RetrofitNetwork(private val networkApi: RetrofitNetworkApi): NetworkDataSource {
    override suspend fun getAnimeList(): NetworkResult<List<Anime>> {
        return safeApiCall {
            val networkResult = networkApi.getAnimeList()
            if(!networkResult.success) throw Exception()
            networkResult.animeList
        }
    }

    override suspend fun getAnimeFacts(name: String): NetworkResult<List<AnimeFact>> {
        return safeApiCall {
            val networkResult = networkApi.getAnimeFacts(name)
            if(!networkResult.success) throw Exception()
            networkResult.animeFacts
        }
    }

}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall.invoke())
    } catch (throwable: Throwable){
        Log.i("longevity-retrofit", "error: $throwable")
        when(throwable){
            is IOException -> NetworkResult.NetworkError
            is HttpException -> {
                val code = throwable.code()
                val errorResponse = throwable.response()?.errorBody()?.source()
                NetworkResult.GenericError(code, errorResponse?.readUtf8())
            }
            else -> NetworkResult.GenericError(null, null)
        }
    }
}