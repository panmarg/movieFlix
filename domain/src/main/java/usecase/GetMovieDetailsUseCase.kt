package usecase

import kotlinx.coroutines.flow.Flow
import model.MovieDetails
import repository.MovieDetailsRepository

class GetMovieDetailsUseCase(private val repository: MovieDetailsRepository) {
    operator fun invoke(movieId: Int): Flow<MovieDetails?> = repository.getMovieDetailsFromDB(movieId = movieId)
}