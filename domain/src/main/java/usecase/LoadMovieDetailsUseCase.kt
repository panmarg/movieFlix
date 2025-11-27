package usecase

import common.ResultResponse
import repository.MovieDetailsRepository

class LoadMovieDetailsUseCase(private val repository: MovieDetailsRepository) {
    suspend operator fun invoke(movieId: Int): ResultResponse<Unit> = repository.loadMovieDetails(movieId = movieId)
}