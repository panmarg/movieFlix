package di

import mapper.MovieMapper
import org.koin.dsl.module
import repository.MovieRepository
import repository.MovieRepositoryImpl

val repositoryModule = module {
    single { MovieMapper(get()) }
    single<MovieRepository> {
        MovieRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }
}