package ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import common.ResultResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.ContentState
import usecase.GetMoviesUseCase
import usecase.LoadMoviesUseCase
import usecase.ToggleFavoriteUseCase

class MoviesViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val loadMoviesUseCase: LoadMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MoviesState())
    val state: StateFlow<MoviesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MoviesEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeMovies()
    }

    fun onIntent(intent: MoviesIntent) {
        when (intent) {
            is MoviesIntent.LoadMovies -> loadNextPage()
            is MoviesIntent.RefreshMovies -> refreshMovies()
            is MoviesIntent.ToggleFavorite -> toggleFavorite(intent.movieId)
            is MoviesIntent.OnInitialLoadMovies -> loadInitialMovies()
            is MoviesIntent.MovieClicked -> movieClicked(intent.movieId)
        }
    }

    private fun movieClicked(movieId: Int) {
        viewModelScope.launch {
            _effect.emit(MoviesEffect.NavigateToMovieDetails(movieId))
        }
    }

    private fun observeMovies() {
        viewModelScope.launch {
            getMoviesUseCase().collect { movies ->
                _state.update { it.copy(movies = movies) }
                if (movies.isEmpty() && !state.value.isPullToRefresh) {
                    loadInitialMovies()
                }
            }
        }
    }

    private fun loadInitialMovies() {
        viewModelScope.launch {
            _state.update { it.copy(contentState = ContentState.Loading, isPullToRefresh = false) }
            try {
                when (val response = loadMoviesUseCase.invoke(1)) {
                    is ResultResponse.Error -> {
                        delay(200)
                        _state.update {
                            it.copy(
                                contentState = ContentState.Error(
                                    response.exception.message ?: "Something went wrong"
                                ),
                                isPullToRefresh = false
                            )
                        }
                    }

                    is ResultResponse.Success -> {
                        _state.update {
                            it.copy(contentState = ContentState.Success, isPullToRefresh = false)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(contentState = ContentState.Error(e.message ?: "Something went wrong"), isPullToRefresh = false)
                }
            }
        }
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            _state.update { it.copy(isPullToRefresh = true, contentState = ContentState.Loading) }
            try {
                when (val response = loadMoviesUseCase.invoke(1)) {
                    is ResultResponse.Error -> {
                        // Note: Delay due to PullToRefreshBox (ExperimentalMaterial3Api)
                        delay(200)
                        _state.update {
                            it.copy(isPullToRefresh = false,  contentState = if(state.value.movies.isNullOrEmpty()) ContentState.Error(response.exception.message ?: "Something went wrong") else ContentState.Success)
                        }
                    }

                    is ResultResponse.Success -> {
                        // Note: Delay due to PullToRefreshBox (ExperimentalMaterial3Api)
                        delay(200)
                        _state.update {
                            it.copy(isPullToRefresh = false, contentState = ContentState.Success, currentPage = 1)
                        }
                    }
                }
            } catch (e: Exception) {
                // Note: Delay due to PullToRefreshBox (ExperimentalMaterial3Api)
                delay(200)
                _state.update {
                    it.copy(isPullToRefresh = false,  contentState = if(state.value.movies.isNullOrEmpty()) ContentState.Error(e.message ?: "Something went wrong") else ContentState.Success)
                }
            }
        }
    }

    private fun loadNextPage() {
        if (state.value.bottomPaginationState == ContentState.Loading) return
        viewModelScope.launch {
            _state.update { it.copy(bottomPaginationState = ContentState.Loading) }
            try {
                when (val response = loadMoviesUseCase.invoke(state.value.currentPage + 1)) {
                    is ResultResponse.Error -> {
                        _state.update {
                            it.copy(
                                bottomPaginationState = ContentState.Error(
                                    response.exception.message ?: "Something went wrong"
                                )
                            )
                        }
                        _effect.emit(MoviesEffect.ShowSnackbar(response.exception.message ?: "Something went wrong"))
                    }

                    is ResultResponse.Success -> {
                        _state.update {
                            it.copy(bottomPaginationState = ContentState.Success, currentPage = it.currentPage + 1)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        bottomPaginationState = ContentState.Error(
                            e.message ?: "Something went wrong"
                        )
                    )
                }
                _effect.emit(MoviesEffect.ShowSnackbar(e.message ?: "Something went wrong"))
            }
        }
    }

    private fun toggleFavorite(movieId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(movieId)
        }
    }
}