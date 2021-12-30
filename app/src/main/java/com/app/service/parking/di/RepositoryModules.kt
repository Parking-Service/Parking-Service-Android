package com.app.service.parking.di

import com.app.service.parking.global.App.Companion.context
import com.app.service.parking.model.repository.local.db.AppDB
import com.app.service.parking.model.repository.local.repository.FavoriteRepository
import com.app.service.parking.model.repository.remote.ParkingLotRepository
import com.app.service.parking.model.repository.remote.ReviewRepository
import com.app.service.parking.model.repository.remote.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val userRepositoryModule = module {
    factory { UserRepository() }
}

val parkingRepositoryModule = module {
    factory { ParkingLotRepository() }
}

val favoriteRepositoryModule = module {
    factory {
        FavoriteRepository(AppDB.getDatabase(context!!))
    }
}

val reviewRepository = module {
    factory {
        ReviewRepository()
    }
}