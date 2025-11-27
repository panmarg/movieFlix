package model

import kotlinx.serialization.Serializable


@Serializable
data class Review(
    val id: String,
    val author: String,
    val avatarPath: String?,
    val content: String,
    val rating: Double?
)
