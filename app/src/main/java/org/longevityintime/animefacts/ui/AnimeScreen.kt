@file:OptIn(ExperimentalLifecycleComposeApi::class)

package org.longevityintime.animefacts.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.longevityintime.animefacts.R
import org.longevityintime.animefacts.model.Anime
import org.longevityintime.animefacts.viewmodel.AnimeUiState
import org.longevityintime.animefacts.viewmodel.AnimeViewModel
import kotlin.math.ceil

@Composable
fun AnimeListScreen(
    animeViewModel: AnimeViewModel = viewModel(factory = AnimeViewModel.Factory),
    onAnimeSelected: (String) -> Unit
) {
    val uiState by animeViewModel.uiState.collectAsStateWithLifecycle()
    when(uiState){
        AnimeUiState.GenericError -> {
            ErrorDialog(stringResource(id = R.string.unknown_error)) {
                animeViewModel.onCloseErrorDialog()
            }
        }
        AnimeUiState.NetworkError -> {
            ErrorDialog(stringResource(id = R.string.network_error)) {
                animeViewModel.onCloseErrorDialog()
            }
        }
        AnimeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(id = R.string.loading_anime_list), Modifier.padding(bottom = 10.dp))
                    LinearProgressIndicator()
                }
            }
        }
        is AnimeUiState.Success -> {
            val animeList = (uiState as AnimeUiState.Success).animeList
            AnimeList(animeList = animeList, selectAnime = { animeName ->
                val animeFactRoute = Routes.ANIME_FACT.substring(0, Routes.ANIME_FACT.indexOf('/'))
                val animeFactPath = "$animeFactRoute/$animeName"
                onAnimeSelected(animeFactPath)
            })
        }
    }
}

@Composable
fun AnimeList(
    animeList: List<Anime>,
    selectAnime: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding()
        .statusBarsPadding()) {
        AnimeAppBar()
        StaggeredVerticalGrid(
            maxColumnWidth = 220.dp,
            modifier = Modifier.padding(4.dp)
        ) {
            animeList.forEach { anime ->
                AnimeScreen(anime = anime, selectAnime = selectAnime)
            }
        }
    }
}
@Composable
fun AnimeScreen(
    anime: Anime,
    selectAnime: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(4.dp),
        color = MaterialTheme.colorScheme.surface,
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .clickable { selectAnime(anime.name) }
        ) {
            NetworkImage(
                url = anime.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(4f / 3f)
            )
            Text(
                text = anime.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun AnimeAppBar(

) {
    TopAppBar(elevation = 0.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.height(80.dp)) {
        Image(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null
        )
//        IconButton(
//            modifier = Modifier.align(Alignment.CenterVertically),
//            onClick = {  }
//        ) {
//            Icon(
//                imageVector = Icons.Filled.AccountCircle,
//                contentDescription = stringResource(id = R.string.label_profile)
//            )
//        }
    }
}

@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    maxColumnWidth: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        check(constraints.hasBoundedWidth) {
            "Unbounded width not supported"
        }
        val columns = ceil(constraints.maxWidth / maxColumnWidth.toPx()).toInt()
        val columnWidth = constraints.maxWidth / columns
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val colHeights = IntArray(columns) { 0 } // track each column's height
        val placeables = measurables.map { measurable ->
            val column = shortestColumn(colHeights)
            val placeable = measurable.measure(itemConstraints)
            colHeights[column] += placeable.height
            placeable
        }

        val height = colHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            val colY = IntArray(columns) { 0 }
            placeables.forEach { placeable ->
                val column = shortestColumn(colY)
                placeable.place(
                    x = columnWidth * column,
                    y = colY[column]
                )
                colY[column] += placeable.height
            }
        }
    }
}
private fun shortestColumn(colHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}