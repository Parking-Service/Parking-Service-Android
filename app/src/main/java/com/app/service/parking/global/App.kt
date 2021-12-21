package com.app.service.parking.global

import android.app.Application
import android.content.Context
import com.app.service.parking.R
import com.app.service.parking.di.*
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        Timber.plant(Timber.DebugTree())
        KakaoSdk.init(this,getString(R.string.kakao_native_key))
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(userRepositoryModule, parkingRepositoryModule))
            modules(listOf(loginViewModelModule, MainViewModelModule, SearchViewModelModule))
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        context = null
    }

    companion object {
        var context: Context? = null
            private set
    }


}
