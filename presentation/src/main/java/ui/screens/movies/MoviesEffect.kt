package ui.screens.movies

sealed class MoviesEffect {
    data class NavigateToMovieDetails(val movieId: Int) : MoviesEffect()
    data class ShowSnackbar(val message: String) : MoviesEffect()
}
