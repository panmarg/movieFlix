package mapper

import db.entity.MovieEntity
import model.Cast
import model.MovieDetails
import model.Review
import model.SimilarMovie
import remote.ApiConfig
import remote.dto.CreditsResponseDto
import remote.dto.MovieDetailsDto
import remote.dto.ReviewsResponseDto
import remote.dto.SimilarMoviesResponseDto

class MovieDetailsMapper(private val config: ApiConfig)  {

    fun detailsDtoToEntity(
        detailsDto: MovieDetailsDto,
        creditsDto: CreditsResponseDto,
        reviewsDto: ReviewsResponseDto,
        similarDto: SimilarMoviesResponseDto
    ): MovieEntity {

        val cast = creditsDto.cast.map { castDto ->
            Cast(
                id = castDto.id,
                name = castDto.name,
                character = castDto.character,
                profilePath = castDto.profilePath?.let { config.imageUrl + it }
            )
        }

        val reviews = reviewsDto.results.take(3).map { reviewDto ->
            Review(
                id = reviewDto.id,
                author = reviewDto.author,
                avatarPath = reviewDto.authorDetails.avatarPath?.let { config.imageUrl + it },
                content = reviewDto.content,
                rating = reviewDto.authorDetails.rating
            )
        }

        val similarMovies = similarDto.results.take(6).map { similarMovieDto ->
            SimilarMovie(
                id = similarMovieDto.id,
                title = similarMovieDto.title,
                posterPath = similarMovieDto.posterPath?.let { config.imageUrl + it }
            )
        }

        return MovieEntity(
            id = detailsDto.id,
            title = detailsDto.title,
            originalTitle = detailsDto.originalTitle,
            overview = detailsDto.overview,
            posterPath = detailsDto.posterPath?.let { config.imageUrl + it },
            backdropPath = detailsDto.backdropPath?.let { config.imageUrl + it },
            releaseDate = detailsDto.releaseDate,
            popularity = detailsDto.popularity,
            voteAverage = detailsDto.voteAverage,
            voteCount = detailsDto.voteCount,
            adult = detailsDto.adult,
            runtime = detailsDto.runtime,
            genres = detailsDto.genres.map { it.name },
            cast = cast,
            reviews = reviews,
            similarMovies = similarMovies,
            homepage = detailsDto.homepage
        )
    }

    fun entityToDetailsDomain(entity: MovieEntity, isFavorite: Boolean): MovieDetails {
        val castDomain = entity.cast.map {
            Cast(
                id = it.id,
                name = it.name,
                character = it.character,
                profilePath = it.profilePath
            )
        }
        val reviewsDomain = entity.reviews.map {
            Review(
                id = it.id,
                author = it.author,
                avatarPath = it.avatarPath,
                content = it.content,
                rating = it.rating
            )
        }
        val similarMoviesDomain = entity.similarMovies.map {
            SimilarMovie(
                id = it.id,
                title = it.title,
                posterPath = it.posterPath
            )
        }

        return MovieDetails(
            id = entity.id,
            title = entity.title,
            originalTitle = entity.originalTitle,
            overview = entity.overview,
            posterPath = entity.posterPath,
            backdropPath = entity.backdropPath,
            releaseDate = entity.releaseDate,
            popularity = entity.popularity,
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount,
            adult = entity.adult,
            runtime = entity.runtime,
            genres = entity.genres,
            cast = castDomain,
            reviews = reviewsDomain,
            similarMovies = similarMoviesDomain,
            isFavorite = isFavorite,
            homepage = entity.homepage
        )
    }
}