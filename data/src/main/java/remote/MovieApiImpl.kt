package remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import remote.dto.MovieResponseDto

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
}