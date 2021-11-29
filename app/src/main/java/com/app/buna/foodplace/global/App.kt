package com.app.buna.foodplace.global

import android.app.Application
import androidx.viewbinding.BuildConfig
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            //modules(viewModelModules)
        }
    }
}