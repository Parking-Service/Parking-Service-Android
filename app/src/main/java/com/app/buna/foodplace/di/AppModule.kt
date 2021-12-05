package com.app.buna.foodplace.di

import com.app.buna.foodplace.feature.login.LoginViewModel
import com.app.buna.foodplace.feature.main.MainViewModel
import com.app.buna.foodplace.feature.main.review.ReviewViewModel
import com.app.buna.foodplace.model.repository.remote.UserRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository() }
}

val loginViewModelModule = module {
    viewModel {
        LoginViewModel(get())
    }
}

val ReviewViewModelModule = module {
    viewModel {
        ReviewViewModel()
    }
}

val MainViewModelModule = module {
    viewModel {
        MainViewModel()
    }
}
