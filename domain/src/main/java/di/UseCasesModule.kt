package di

import org.koin.dsl.module
import usecase.GetMovieDetailsUseCase
import usecase.GetMoviesUseCase
import usecase.LoadMovieDetailsUseCase
import usecase.LoadMoviesUseCase
import usecase.ToggleFavoriteUseCase

val useCasesModule = module {
    single { GetMoviesUseCase(get()) }
    single { LoadMoviesUseCase(get()) }
    single { ToggleFavoriteUseCase(get()) }
    single { GetMovieDetailsUseCase(get()) }
    single { LoadMovieDetailsUseCase(get())}
}