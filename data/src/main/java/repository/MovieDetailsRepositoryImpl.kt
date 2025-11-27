package repository

import common.ResultResponse
import db.dao.FavoriteDao
import db.dao.MovieDao
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import mapper.MovieDetailsMapper
import model.MovieDetails
import remote.MovieApi

class MovieDetailsRepositoryImpl(
    private val api: MovieApi,
    private val movieDao: MovieDao,
    private val favoriteDao: FavoriteDao,
    private val mapper: MovieDetailsMapper
) : MovieDetailsRepository {

    override fun getMovieDetailsFromDB(movieId: Int): Flow<MovieDetails?> =
        combine(
            movieDao.getMovieFlow(movieId),
            favoriteDao.getAllFavoritesFlow()
        ) { movieEntity, favorites ->
            movieEntity?.let { movie ->
                val isFavorite = favorites.any { it.movieId == movieId }
                mapper.entityToDetailsDomain(movie, isFavorite)
            }
        }

    override suspend fun loadMovieDetails(movieId: Int): ResultResponse<Unit> {
        return try {
            coroutineScope {
                val detailsDeferred = async { api.getMovieDetails(movieId) }
                val creditsDeferred = async { api.getMovieCredits(movieId) }
                val reviewsDeferred = async { api.getMovieReviews(movieId, page = 1) }
                val similarDeferred = async { api.getSimilarMovies(movieId, page = 1) }

                val detailsDto = detailsDeferred.await()
                val creditsDto = creditsDeferred.await()
                val reviewsDto = reviewsDeferred.await()
                val similarDto = similarDeferred.await()
                val entity = mapper.detailsDtoToEntity(
                    detailsDto = detailsDto,
                    creditsDto = creditsDto,
                    reviewsDto = reviewsDto,
                    similarDto = similarDto
                )
                movieDao.insert(entity)
                ResultResponse.Success(Unit)
            }
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }

}