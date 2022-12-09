@file:OptIn(ExperimentalLifecycleComposeApi::class)

package org.longevityintime.animefacts.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.longevityintime.animefacts.R
import org.longevityintime.animefacts.model.AnimeFact
import org.longevityintime.animefacts.model.AnimeFacts
import org.longevityintime.animefacts.viewmodel.AnimeFactUiState
import org.longevityintime.animefacts.viewmodel.AnimeFactVieModel

@Composable
fun AnimeFactsScreen(
    viewModel: AnimeFactVieModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when(uiState){
        AnimeFactUiState.GenericError -> {
            ErrorDialog(stringResource(id = R.string.unknown_error)) {
                viewModel.onCloseErrorDialog()
            }
        }
        AnimeFactUiState.NetworkError -> {
            ErrorDialog(stringResource(id = R.string.network_error)) {
                viewModel.onCloseErrorDialog()
            }
        }
        AnimeFactUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(id = R.string.loading_anime_facts), Modifier.padding(bottom = 10.dp))
                    LinearProgressIndicator()
                }
            }
        }
        is AnimeFactUiState.Success -> {
            val animeFacts = (uiState as AnimeFactUiState.Success).animeFacts
            AnimeFacts(animeFacts = animeFacts)
        }
    }
}

@Composable
fun AnimeFacts(
    animeFacts: AnimeFacts,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding()
        .statusBarsPadding()) {
        AnimeHeader(animeFacts.animeImgUrl)
        animeFacts.facts.forEach { animeFact ->
            AnimeFactScreen(animeFact = animeFact)
        }
    }
}
@Composable
fun AnimeHeader(
    animeImgUrl: String,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier.padding(4.dp),
        color = MaterialTheme.colorScheme.surface,
        elevation = 2.dp,
    ) {
        Column {
            NetworkImage(
                url = animeImgUrl,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(4f / 3f)
            )
        }
    }
}
@Composable
fun AnimeFactScreen(
    animeFact: AnimeFact,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(4.dp),
        color = MaterialTheme.colorScheme.surface,
        elevation = 2.dp,
    ) {
        Text(
            text = animeFact.fact,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}