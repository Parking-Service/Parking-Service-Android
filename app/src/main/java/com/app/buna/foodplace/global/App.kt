package com.app.buna.foodplace.global

import android.app.Application
import android.content.Context
import com.app.buna.foodplace.R
import com.app.buna.foodplace.di.contextModule
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(Timber.DebugTree())
        KakaoSdk.init(this,getString(R.string.kakao_native_key))
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
        }

    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
            private set
    }


}
