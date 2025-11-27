package remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsResponseDto(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class Review(
    val author: String,
    @SerialName("author_details") val authorDetails: AuthorDetails,
    val content: String,
    @SerialName("created_at") val createdAt: String,
    val id: String,
    @SerialName("updated_at") val updatedAt: String,
    val url: String
)

@Serializable
data class AuthorDetails(
    val name: String,
    val username: String,
    @SerialName("avatar_path") val avatarPath: String?,
    val rating: Double?
)