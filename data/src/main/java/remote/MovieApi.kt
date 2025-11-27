package remote

import remote.dto.MovieResponseDto

interface MovieApi {
    suspend fun getPopularMovies(page: Int = 1): MovieResponseDto
}