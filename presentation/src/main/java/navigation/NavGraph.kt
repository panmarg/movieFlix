package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ui.screens.details.Actor
import ui.screens.details.MovieDetails
import ui.screens.details.MovieDetailsScreen
import ui.screens.details.Review
import ui.screens.details.SimilarMovie
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
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailsScreen(
                movie = MovieDetails(
                    title = "The Dark Knight",
                    year = 2008,
                    rating = 9.0f,
                    duration = "152 min",
                    genres = listOf("Action", "Crime", "Drama"),
                    synopsis = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
                    cast = listOf(
                        Actor("Christian Bale", "https://image.tmdb.org/t/p/w200/i1U0t2GzJjA7g4g9zN1A2l0A2j.jpg"),
                        Actor("Heath Ledger", "https://image.tmdb.org/t/p/w200/5Y9HnYYa9jF4NunY9lSgJG1bTz.jpg"),
                        Actor("Aaron Eckhart", "https://image.tmdb.org/t/p/w200/6y7lC2x2h32I4y0a1s9s2H9s6o.jpg"),
                        Actor("Maggie Gyllenhaal", "https://image.tmdb.org/t/p/w200/2A1Ywkalitb2aW3f9aP6yO0ehi1.jpg")
                    ),
                    reviews = listOf(
                        Review("John Smith", "", 9.5f, "Masterpiece of cinema. Heath Ledger's performance as the Joker is absolutely phenomenal."),
                        Review("Sarah Wilson", "", 8.8f, "Dark, intense, and brilliantly crafted. One of the best superhero movies ever made.")
                    ),
                    similarMovies = listOf(
                        SimilarMovie("Batman Begins", 2005, "https://image.tmdb.org/t/p/w200/15s2YxKk5r3sT33KzG3s0kM3f6.jpg"),
                        SimilarMovie("The Dark Knight Rises", 2012, "https://image.tmdb.org/t/p/w200/vL5d2eG3z1P2Vb4d4b2n3sJv.jpg"),
                        SimilarMovie("Joker", 2019, "https://image.tmdb.org/t/p/w200/n6bUvigp6d_3fICKkbz3mU4j5a.jpg")
                    ),
                    posterUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg"
                ),
                movieId = movieId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}