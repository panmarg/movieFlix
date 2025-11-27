package ui.screens.details

sealed class MovieDetailsIntent {
    data class SetMovieId(val movieId: Int) : MovieDetailsIntent()
    object OnGoBackClick : MovieDetailsIntent()
    object OnShareClick : MovieDetailsIntent()

    data class ToggleFavorite(val movieId: Int) : MovieDetailsIntent()

    object OnReceiveEmptyMovieId : MovieDetailsIntent()
}


