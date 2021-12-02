package com.app.buna.foodplace.global

import android.app.Application
import android.content.Context
import com.app.buna.foodplace.R
import com.app.buna.foodplace.di.repositoryModule
import com.app.buna.foodplace.di.viewModelModule
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
            modules(repositoryModule)
            modules(viewModelModule)
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
