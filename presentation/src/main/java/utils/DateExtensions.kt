package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toUiDate(): String {
    return try {
        val input = LocalDate.parse(this)
        input.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    } catch (e: Exception) {
        this
    }
}