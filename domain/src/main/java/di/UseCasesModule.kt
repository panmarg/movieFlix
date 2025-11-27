package di

import org.koin.dsl.module
import usecase.GetMoviesUseCase
import usecase.LoadMoviesUseCase
import usecase.ToggleFavoriteUseCase

val useCasesModule = module {
    single { GetMoviesUseCase(get()) }
    single { LoadMoviesUseCase(get()) }
    single { ToggleFavoriteUseCase(get()) }
}