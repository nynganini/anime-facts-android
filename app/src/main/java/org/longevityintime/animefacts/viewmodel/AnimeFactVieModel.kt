package org.longevityintime.animefacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.longevityintime.animefacts.model.Anime
import org.longevityintime.animefacts.model.AnimeFact
import org.longevityintime.animefacts.model.AnimeFacts
import org.longevityintime.animefacts.retrofit.DI
import org.longevityintime.animefacts.retrofit.NetworkDataSource
import org.longevityintime.animefacts.retrofit.NetworkResult

class AnimeFactVieModel(val animeName: String, private val network: NetworkDataSource): ViewModel() {

    private var _uiState: MutableStateFlow<AnimeFactUiState> = MutableStateFlow(AnimeFactUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        refreshUi()
    }

    private fun refreshUi(){
        viewModelScope.launch {
            when (val networkResult = network.getAnimeFacts(animeName)) {
                is NetworkResult.GenericError -> _uiState.value = AnimeFactUiState.GenericError
                is NetworkResult.NetworkError -> _uiState.value = AnimeFactUiState.NetworkError
                is NetworkResult.Success -> _uiState.value = AnimeFactUiState.Success(networkResult.value)
            }
        }
    }

    fun onCloseErrorDialog() {
        _uiState.value = AnimeFactUiState.Loading
        refreshUi()
    }

    companion object {
        private object AnimeImpl: CreationExtras.Key<String>
        val ANIME_KEY: CreationExtras.Key<String> = AnimeImpl

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val anime = (this[ANIME_KEY] as String)
                AnimeFactVieModel(anime, DI.networkDataSource)
            }
        }
    }
}

sealed interface AnimeFactUiState {
    object Loading: AnimeFactUiState
    data class Success(val animeFacts: AnimeFacts): AnimeFactUiState
    object NetworkError: AnimeFactUiState
    object GenericError: AnimeFactUiState
}