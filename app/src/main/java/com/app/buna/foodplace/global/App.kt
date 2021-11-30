package com.app.buna.foodplace.global

import android.app.Application
import androidx.viewbinding.BuildConfig
import org.koin.core.context.startKoin
import org.koin.core.logger.Logger

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

        }
    }
}