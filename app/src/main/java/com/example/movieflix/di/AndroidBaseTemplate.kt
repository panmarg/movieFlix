package com.example.movieflix.di

import android.app.Application
import di.dataModule
import di.domainModule
import di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
class AndroidBaseTemplate : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AndroidBaseTemplate)
            modules(appModule + dataModule + domainModule + presentationModule)
        }
    }

}