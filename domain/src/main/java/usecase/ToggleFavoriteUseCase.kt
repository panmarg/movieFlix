package usecase

import repository.MovieRepository

class ToggleFavoriteUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int) = repository.toggleFavorite(movieId)
}