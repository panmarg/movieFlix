package model

import kotlinx.serialization.Serializable

@Serializable
data class SimilarMovie(
    val id: Int,
    val title: String,
    val posterPath: String?
)
