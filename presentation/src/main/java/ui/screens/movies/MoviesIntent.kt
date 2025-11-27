package ui.screens.movies

sealed class MoviesIntent {

    object OnInitialLoadMovies: MoviesIntent()
    object LoadMovies : MoviesIntent()
    object RefreshMovies : MoviesIntent()
    data class ToggleFavorite(val movieId: Int) : MoviesIntent()

    data class MovieClicked(val movieId: Int) : MoviesIntent()


}