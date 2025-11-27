package usecase

import common.ResultResponse
import kotlinx.coroutines.flow.Flow
import model.Movie
import repository.MovieRepository

class LoadMoviesUseCase(private val repository: MovieRepository) {
     suspend operator fun invoke(page: Int = 1): ResultResponse<List<Movie>> = repository.loadMovies(page)
}