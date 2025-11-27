package remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import remote.dto.CreditsResponseDto
import remote.dto.MovieDetailsDto
import remote.dto.MovieResponseDto
import remote.dto.ReviewsResponseDto
import remote.dto.SimilarMoviesResponseDto

class MovieApiImpl(
    private val client: HttpClient,
    private val config: ApiConfig
) : MovieApi {

    override suspend fun getPopularMovies(page: Int): MovieResponseDto {
        return client.get("${config.baseUrl}/movie/popular") {
            header("Authorization", "Bearer ${config.bearerToken}")
            parameter("language", "en-US")
            parameter("page", page)
        }.body()
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsDto {
        return client.get("${config.baseUrl}/movie/$movieId") {
            header("Authorization", "Bearer ${config.bearerToken}")
            parameter("language", "en-US")
        }.body()
    }

    override suspend fun getMovieCredits(movieId: Int): CreditsResponseDto {
        return client.get("${config.baseUrl}/movie/$movieId/credits") {
            header("Authorization", "Bearer ${config.bearerToken}")
            parameter("language", "en-US")
        }.body()
    }

    override suspend fun getMovieReviews(movieId: Int, page: Int): ReviewsResponseDto {
        return client.get("${config.baseUrl}/movie/$movieId/reviews") {
            header("Authorization", "Bearer ${config.bearerToken}")
            parameter("language", "en-US")
            parameter("page", page)
        }.body()
    }

    override suspend fun getSimilarMovies(movieId: Int, page: Int): SimilarMoviesResponseDto {
        return client.get("${config.baseUrl}/movie/$movieId/similar") {
            header("Authorization", "Bearer ${config.bearerToken}")
            parameter("language", "en-US")
            parameter("page", page)
        }.body()
    }
}
