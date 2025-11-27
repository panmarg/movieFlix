package db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import db.dao.FavoriteDao
import db.dao.MovieDao
import db.entity.FavoriteEntity
import db.entity.MovieEntity

@Database(
    entities = [MovieEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
}