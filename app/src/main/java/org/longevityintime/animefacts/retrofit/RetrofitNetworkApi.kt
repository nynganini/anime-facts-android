package org.longevityintime.animefacts.retrofit

import android.util.Log
import org.longevityintime.animefacts.model.Anime
import org.longevityintime.animefacts.model.AnimeFact
import org.longevityintime.animefacts.model.AnimeFacts
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException

interface RetrofitNetworkApi {
    @GET("v1/")
    suspend fun getAnimeList(): AnimeListWrapper
    @GET("v1/{animeName}")
    suspend fun getAnimeFacts(@Path("animeName") name: String): AnimeFactsWrapper
}

class RetrofitNetwork(private val networkApi: RetrofitNetworkApi): NetworkDataSource {
    override suspend fun getAnimeList(): NetworkResult<List<Anime>> {
        return safeApiCall {
            val networkResult = networkApi.getAnimeList()
            if(!networkResult.success) throw Exception()
            networkResult.animeList
        }
    }

    override suspend fun getAnimeFacts(name: String): NetworkResult<AnimeFacts> {
        return safeApiCall {
            val networkResult = networkApi.getAnimeFacts(name)
            if(!networkResult.success) throw Exception()
            AnimeFacts(networkResult.animeImageUrl, networkResult.animeFacts)
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