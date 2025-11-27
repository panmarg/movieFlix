package ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import common.ResultResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.MovieDetails
import ui.ContentState
import usecase.GetMovieDetailsUseCase
import usecase.LoadMovieDetailsUseCase
import usecase.ToggleFavoriteUseCase

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val loadMovieDetailsUseCase: LoadMovieDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MovieDetailsState())
    val state: StateFlow<MovieDetailsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MovieDetailsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeMovieDetails()
    }

    fun onIntent(intent: MovieDetailsIntent) {
        when (intent) {
            is MovieDetailsIntent.OnGoBackClick -> {
                onGoBackClick()
            }

            is MovieDetailsIntent.OnShareClick -> {
                onShareClick()
            }

            is MovieDetailsIntent.ToggleFavorite -> {
                toggleFavorite(intent.movieId)
            }

            is MovieDetailsIntent.SetMovieId -> {
                if (state.value.movieIdFromArgs == null) {
                    _state.update { it.copy(movieIdFromArgs = intent.movieId) }
                    loadMovieDetails()
                }
            }

            is MovieDetailsIntent.OnReceiveEmptyMovieId -> {
                _state.update {
                    it.copy(
                        contentState = ContentState.Error(
                            message = "This movie ID doesn't exist in our DB. Try going back and try again."
                        )
                    )
                }
            }
        }
    }

    private fun onGoBackClick() {
        viewModelScope.launch {
            _effect.emit(MovieDetailsEffect.GoBack)
        }
    }

    private fun onShareClick() {
        viewModelScope.launch {
            _effect.emit(MovieDetailsEffect.GoToShareMovie)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeMovieDetails() {
        viewModelScope.launch {
            state
                .map { it.movieIdFromArgs }
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { movieId ->
                    getMovieDetailsUseCase(movieId)
                }
                .collect { movie ->
                    if (movie != null) {
                        updateUIFlags(movie)
                    }
                }
        }
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            _state.update { it.copy(contentState = ContentState.Loading) }
            when (val result = loadMovieDetailsUseCase.invoke(state.value.movieIdFromArgs ?: 0)) {
                is ResultResponse.Success -> {
                    _state.update { it.copy(contentState = ContentState.Success) }
                }
                is ResultResponse.Error -> {
                    if (state.value.movieIdFromArgs != null) {
                        _state.update {
                            it.copy(contentState = ContentState.Success)
                        }
                    } else {
                        _state.update {
                            it.copy(
                                contentState = ContentState.Error(
                                    message = result.exception.message
                                        ?: "Failed to load movie details"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun toggleFavorite(movieId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(movieId)
        }
    }

    private fun updateUIFlags(movie: MovieDetails) {

        val showHeaderSection = movie.title != null || movie.posterPath != null || movie.releaseDate != null || movie.genres != null || movie.voteAverage != null
        val showOverviewSection = !movie.overview.isNullOrEmpty()
        val showCastSection = !movie.cast.isNullOrEmpty()
        val showReviewsSection = !movie.reviews.isNullOrEmpty()
        val showSimilarMoviesSection = !movie.similarMovies.isNullOrEmpty()
        val showShareSection = !movie.homepage.isNullOrEmpty()

        _state.update {
            it.copy(
                movieDetails = movie,
                showHeaderSection = showHeaderSection,
                showOverviewSection = showOverviewSection,
                showCastSection = showCastSection,
                showReviewsSection = showReviewsSection,
                showSimilarMoviesSection = showSimilarMoviesSection,
                showShareSection = showShareSection
            )
        }
    }


}