package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ui.screens.details.MovieDetailsScreen
import ui.screens.movies.MoviesScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MOVIES
    ) {
        composable(Routes.MOVIES) {
            MoviesScreen(
                onMovieClick = { movieId ->
                    navController.navigate(
                        Routes.MOVIE_DETAILS.replace(
                            "{movieId}",
                            movieId.toString()
                        )
                    )
                }
            )
        }
        composable(
            Routes.MOVIE_DETAILS,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            MovieDetailsScreen(
                movieId = movieId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}