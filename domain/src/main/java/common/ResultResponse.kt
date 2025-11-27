package common

sealed class ResultResponse<out T> {
    data class Success<T>(val data: T) : ResultResponse<T>()
    data class Error(val exception: Throwable) : ResultResponse<Nothing>()
}