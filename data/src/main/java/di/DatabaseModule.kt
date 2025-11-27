package di

import android.content.Context
import androidx.room.Room
import db.MovieDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>(),
            MovieDatabase::class.java,
            "movie_db"
        ).fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<MovieDatabase>().movieDao() }
    single { get<MovieDatabase>().favoriteDao() }
}