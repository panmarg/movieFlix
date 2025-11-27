package ui

sealed class ContentState {
    object Initial : ContentState()
    object Loading : ContentState()
    object Success : ContentState()
    data class Error(val message: String) : ContentState()
}