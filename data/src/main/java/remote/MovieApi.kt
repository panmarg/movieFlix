package remote

import remote.dto.CreditsResponseDto
import remote.dto.MovieDetailsDto
import remote.dto.MovieResponseDto
import remote.dto.ReviewsResponseDto
import remote.dto.SimilarMoviesResponseDto

interface MovieApi {
    suspend fun getPopularMovies(page: Int): MovieResponseDto
    suspend fun getMovieDetails(movieId: Int): MovieDetailsDto
    suspend fun getMovieCredits(movieId: Int): CreditsResponseDto
    suspend fun getMovieReviews(movieId: Int, page: Int = 1): ReviewsResponseDto
    suspend fun getSimilarMovies(movieId: Int, page: Int = 1): SimilarMoviesResponseDto
}
