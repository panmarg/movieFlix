package remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditsResponseDto(
    val cast: List<CastDto>,
    val crew: List<CrewDto>
)

@Serializable
data class CastDto(
    val id: Int,
    val name: String,
    val character: String?,
    @SerialName("profile_path") val profilePath: String?
)

@Serializable
data class CrewDto(
    val id: Int,
    val name: String,
    val job: String,
    @SerialName("profile_path") val profilePath: String?
)