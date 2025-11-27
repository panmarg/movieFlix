package ui.screens.movies

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ui.ContentState
import ui.components.ErrorSection
import ui.components.movies.MovieItem
import ui.components.movies.ShimmerMovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MoviesViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    val shouldLoadMore by remember {
        derivedStateOf {
            val movies = state.movies
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            movies != null && movies.isNotEmpty() && lastVisible >= movies.size - 2
        }
    }

    LaunchedEffect(shouldLoadMore, state.bottomPaginationState) {
        if (shouldLoadMore && state.bottomPaginationState != ContentState.Loading) {
            viewModel.onIntent(MoviesIntent.LoadMovies)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MoviesEffect.NavigateToMovieDetails -> onMovieClick(effect.movieId)
                is MoviesEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    LaunchedEffect(state) {
        Log.d(
            "MoviesVM",
            "Screen moviesCount=${state.movies?.size}, " +
                    "currentPage=${state.currentPage}, " +
                    "contentState=${state.contentState}, " +
                    "isPullToRefresh=${state.isPullToRefresh}, " +
                    "bottomPaginationState=${state.bottomPaginationState}"
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = state.isPullToRefresh,
            onRefresh = { viewModel.onIntent(MoviesIntent.RefreshMovies) },
            modifier = Modifier.padding(paddingValues)
        ) {
            when (state.contentState) {
                ContentState.Loading -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        userScrollEnabled = false
                    ) {
                        items(10) {
                            ShimmerMovieItem()
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                is ContentState.Error -> {
                    ErrorSection(
                        message = (state.contentState as ContentState.Error).message,
                        onRetry = { viewModel.onIntent(MoviesIntent.OnInitialLoadMovies) }
                    )
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                    ) {
                        items(state.movies ?: listOf(), key = { it.id }) { movie ->
                            MovieItem(
                                movie = movie,
                                onFavoriteClick = {
                                    viewModel.onIntent(MoviesIntent.ToggleFavorite(movie.id))
                                },
                                onMovieClick = {
                                    onMovieClick.invoke(movie.id)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            when (state.bottomPaginationState) {
                                ContentState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}


