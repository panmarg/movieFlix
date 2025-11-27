package ui.screens.details

import model.MovieDetails
import ui.ContentState

data class MovieDetailsState(
    val movieIdFromArgs: Int? = null,
    val movieUrl: String? = null,
    val movieDetails: MovieDetails = MovieDetails(),
    val contentState: ContentState = ContentState.Initial,
    val showHeaderSection: Boolean = false,
    val showOverviewSection: Boolean = false,
    val showShareSection: Boolean = false,
    val showCastSection: Boolean = false,
    val showReviewsSection: Boolean = false,
    val showSimilarMoviesSection: Boolean = false,
)

