package ui.screens.details

sealed class MovieDetailsEffect {
    object GoBack : MovieDetailsEffect()
    object GoToShareMovie : MovieDetailsEffect()
}