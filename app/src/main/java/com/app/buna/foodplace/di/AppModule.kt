package com.app.buna.foodplace.di

import com.app.buna.foodplace.feature.login.LoginViewModel
import com.app.buna.foodplace.model.repository.remote.UserRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository() }
}

val viewModelModule = module {
    viewModel {
        LoginViewModel(get())
    }
}