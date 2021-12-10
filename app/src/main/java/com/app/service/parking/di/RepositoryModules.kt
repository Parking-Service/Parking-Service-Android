package com.app.service.parking.di

import com.app.service.parking.model.repository.remote.ParkingLotRepository
import com.app.service.parking.model.repository.remote.UserRepository
import org.koin.dsl.module

val userRepositoryModule = module {
    factory { UserRepository() }
}

val parkingRepositoryModule = module {
    factory { ParkingLotRepository() }
}