package ui.screens.movies

import model.Movie
import ui.ContentState

data class MoviesState(
    val movies: List<Movie>? = null,
    val currentPage: Int = 1,
    val contentState: ContentState = ContentState.Initial,
    val isPullToRefresh: Boolean = false,
    val bottomPaginationState: ContentState = ContentState.Initial,
)