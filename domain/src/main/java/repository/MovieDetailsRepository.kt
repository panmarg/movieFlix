package repository

import common.ResultResponse
import kotlinx.coroutines.flow.Flow
import model.MovieDetails

interface MovieDetailsRepository {
    fun getMovieDetailsFromDB(movieId: Int): Flow<MovieDetails?>
    suspend fun loadMovieDetails(movieId: Int): ResultResponse<Unit>
}