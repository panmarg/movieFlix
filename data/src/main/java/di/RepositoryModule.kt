package di

import mapper.MovieDetailsMapper
import mapper.MoviesMapper
import org.koin.dsl.module
import repository.MovieDetailsRepository
import repository.MovieDetailsRepositoryImpl
import repository.MovieRepository
import repository.MovieRepositoryImpl

val repositoryModule = module {
    single { MoviesMapper(get()) }
    single<MovieRepository> {
        MovieRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }
    single { MovieDetailsMapper(get()) }
    single<MovieDetailsRepository> {
        MovieDetailsRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }
}