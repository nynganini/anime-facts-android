package org.longevityintime.animefacts.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.longevityintime.animefacts.viewmodel.AnimeFactVieModel
import org.longevityintime.animefacts.viewmodel.AnimeViewModel

object Routes {
    const val ANIME = "anime"
    const val ANIME_FACT = "anime_fact/{anime_name}"
    const val LOGIN = "login"
}

@Composable
fun AnimeNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = Routes.ANIME,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        animeGraph { destination ->
            navController.navigate(destination)
        }
        animeFactGraph()
    }
}

fun NavGraphBuilder.animeGraph(onAnimeSelected: (String) -> Unit) {
    val extras = MutableCreationExtras()
    val viewModel = AnimeViewModel.Factory.create(AnimeViewModel::class.java, extras)
    composable(route = Routes.ANIME){
        AnimeListScreen(viewModel = viewModel, onAnimeSelected = onAnimeSelected)
    }
}
fun NavGraphBuilder.animeFactGraph() {
    composable(
        route = Routes.ANIME_FACT,
        arguments = listOf(
            navArgument("anime_name") { type = NavType.StringType }
        )
    ){
        val animeName = it.arguments?.getString("anime_name")!!
        val extras = MutableCreationExtras().apply {
            set(AnimeFactVieModel.ANIME_KEY, animeName)
        }
        val viewModel = AnimeFactVieModel.Factory.create(AnimeFactVieModel::class.java, extras)
        AnimeFactsScreen(viewModel = viewModel)
    }
}