package usecase

import kotlinx.coroutines.flow.Flow
import model.Movie
import repository.MovieRepository

class GetMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Movie>> = repository.getMoviesFromDB()
}