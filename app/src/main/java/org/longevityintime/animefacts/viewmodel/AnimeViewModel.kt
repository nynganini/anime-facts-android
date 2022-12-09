package org.longevityintime.animefacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.longevityintime.animefacts.model.Anime
import org.longevityintime.animefacts.retrofit.DI
import org.longevityintime.animefacts.retrofit.NetworkDataSource
import org.longevityintime.animefacts.retrofit.NetworkResult
import org.longevityintime.animefacts.retrofit.RetrofitNetwork

class AnimeViewModel(private val network: NetworkDataSource): ViewModel() {

    private var _uiState: MutableStateFlow<AnimeUiState> = MutableStateFlow(AnimeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        refreshUi()
    }

    private fun refreshUi(){
        viewModelScope.launch {
            when (val networkResult = network.getAnimeList()) {
                is NetworkResult.GenericError -> _uiState.value = AnimeUiState.GenericError
                is NetworkResult.NetworkError -> _uiState.value = AnimeUiState.NetworkError
                is NetworkResult.Success -> _uiState.value = AnimeUiState.Success(networkResult.value)
            }
        }
    }

    fun onCloseErrorDialog() {
        _uiState.value = AnimeUiState.Loading
        refreshUi()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AnimeViewModel(DI.networkDataSource)
            }
        }
    }

}

sealed interface AnimeUiState {
    object Loading: AnimeUiState
    data class Success(val animeList: List<Anime>): AnimeUiState
    object NetworkError: AnimeUiState
    object GenericError: AnimeUiState
}