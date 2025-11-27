package repository

import common.ResultResponse
import db.dao.FavoriteDao
import db.dao.MovieDao
import db.entity.FavoriteEntity
import db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import mapper.MoviesMapper
import model.Movie
import remote.MovieApi

class MovieRepositoryImpl(
    private val api: MovieApi,
    private val movieDao: MovieDao,
    private val favoriteDao: FavoriteDao,
    private val mapper: MoviesMapper
) : MovieRepository {

    override fun getMoviesFromDB(): Flow<List<Movie>> =
        combine(
            movieDao.getAllMoviesFlow(),
            favoriteDao.getAllFavoritesFlow()
        ) { movies, favorites ->
            val favoriteIds = favorites.map { it.movieId }.toSet()
            movies.map { entity ->
                mapper.entityToDomain(entity, entity.id in favoriteIds)
            }
        }

    override suspend fun loadMovies(page: Int): ResultResponse<List<Movie>> {
        return try {
            val response = api.getPopularMovies(page)

            val favoriteIds: Set<Int> = favoriteDao.getAllFavoritesFlow().first().map { it.movieId }.toSet()

            val entities: List<MovieEntity> = response.results.map { dto ->
                mapper.dtoToEntity(dto)
            }

            if (page == 1) movieDao.clearAll()

            movieDao.insertAll(entities)

            val movies: List<Movie> = entities.map { entity ->
                val isFav = entity.id in favoriteIds
                mapper.entityToDomain(entity, isFav)
            }

            ResultResponse.Success(movies)
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }


    override suspend fun toggleFavorite(movieId: Int) {
        val isFav = favoriteDao.isFavorite(movieId)

        if (isFav) {
            favoriteDao.delete(FavoriteEntity(movieId))
        } else {
            favoriteDao.insert(FavoriteEntity(movieId))
        }
    }
}
