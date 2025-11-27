package db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import model.Cast
import model.SimilarMovie
import model.Review

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val popularity: Double,
    val voteAverage: Double,
    val voteCount: Int,
    val adult: Boolean,
    val homepage: String? = null,
    val genres: List<String> = emptyList(),
    val runtime: Int? = null,
    val cast: List<Cast> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val similarMovies: List<SimilarMovie> = emptyList()
)
