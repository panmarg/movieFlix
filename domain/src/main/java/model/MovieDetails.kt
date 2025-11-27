package model

data class MovieDetails(
    val id: Int? = null,
    val title: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val popularity: Double? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val adult: Boolean = false,
    val runtime: Int? = null,
    val genres: List<String>? = null,
    val cast: List<Cast>? = null,
    val reviews: List<Review>? = null,
    val similarMovies: List<SimilarMovie>? = null,
    val isFavorite: Boolean = false,
    val homepage: String? = null
)
