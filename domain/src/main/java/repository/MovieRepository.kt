package repository

import common.ResultResponse
import kotlinx.coroutines.flow.Flow
import model.Movie

interface MovieRepository {
    fun getMoviesFromDB(): Flow<List<Movie>>
    suspend fun loadMovies(page: Int): ResultResponse<List<Movie>>
    suspend fun toggleFavorite(movieId: Int)
}