package di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ui.screens.details.MovieDetailsViewModel
import ui.screens.movies.MoviesViewModel

val viewModelsModule = module {
    viewModel { MoviesViewModel(get(), get(), get()) }
    viewModel { MovieDetailsViewModel(get(), get(), get()) }
}