package com.app.service.parking.di

import com.app.service.parking.model.repository.remote.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository() }
}