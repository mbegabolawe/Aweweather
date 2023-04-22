package com.example.aweweather

import android.app.Application
import com.example.aweweather.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class AweWeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin{
            androidContext(this@AweWeatherApplication)
            androidLogger(Level.ERROR)
            modules(allModules)
        }
    }
}