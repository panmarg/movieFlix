package db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import model.Cast
import model.Review
import model.SimilarMovie


class Converters {

    @TypeConverter
    fun fromStringList(list: List<String>): String = list.joinToString("|")

    @TypeConverter
    fun toStringList(data: String): List<String> =
        if (data.isEmpty()) emptyList() else data.split("|")

    @TypeConverter
    fun fromCastList(list: List<Cast>): String = Json.encodeToString(list)

    @TypeConverter
    fun toCastList(data: String): List<Cast> = Json.decodeFromString(data)

    @TypeConverter
    fun fromReviewList(list: List<Review>): String = Json.encodeToString(list)

    @TypeConverter
    fun toReviewList(data: String): List<Review> = Json.decodeFromString(data)

    @TypeConverter
    fun fromSimilarList(list: List<SimilarMovie>): String = Json.encodeToString(list)

    @TypeConverter
    fun toSimilarList(data: String): List<SimilarMovie> = Json.decodeFromString(data)
}
