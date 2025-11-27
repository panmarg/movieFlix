package com.example.movieflix.di

import com.example.movieflix.BuildConfig
import org.koin.dsl.module
import remote.ApiConfig

val apiConfigModule = module {
    single {
        ApiConfig(
            baseUrl = BuildConfig.TMDB_BASE_URL,
            bearerToken = BuildConfig.TMDB_BEARER_TOKEN,
            imageUrl = BuildConfig.TMDB_IMAGE_URL
        )
    }
}