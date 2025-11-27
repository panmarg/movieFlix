package mapper

import db.entity.MovieEntity
import model.Movie
import remote.ApiConfig
import remote.dto.MovieDto

class MoviesMapper(private val config: ApiConfig) {

    fun dtoToEntity(dto: MovieDto): MovieEntity = MovieEntity(
        id = dto.id,
        title = dto.title,
        originalTitle = dto.originalTitle,
        overview = dto.overview,
        posterPath = dto.posterPath?.let { config.imageUrl + it },
        backdropPath = dto.backdropPath?.let { config.imageUrl + it },
        releaseDate = dto.releaseDate,
        popularity = dto.popularity,
        voteAverage = dto.voteAverage,
        voteCount = dto.voteCount,
        adult = dto.adult,

    )

    fun entityToDomain(entity: MovieEntity, isFavorite: Boolean): Movie = Movie(
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
        isFavorite = isFavorite
    )

}

