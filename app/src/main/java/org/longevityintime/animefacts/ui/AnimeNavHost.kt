package org.longevityintime.animefacts.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.longevityintime.animefacts.viewmodel.AnimeFactVieModel

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
        animeFactGraph {
            navController.popBackStack()
        }
    }
}

fun NavGraphBuilder.animeGraph(onAnimeSelected: (String) -> Unit) {
    composable(route = Routes.ANIME){
        AnimeListScreen(onAnimeSelected = onAnimeSelected)
    }
}
fun NavGraphBuilder.animeFactGraph(onBack: () -> Unit) {
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
        val animeFactVieModel: AnimeFactVieModel = viewModel(
            factory = AnimeFactVieModel.Factory,
            extras = extras
        )
        AnimeFactsScreen(animeFactViewModel = animeFactVieModel, onBack = onBack)
    }
}