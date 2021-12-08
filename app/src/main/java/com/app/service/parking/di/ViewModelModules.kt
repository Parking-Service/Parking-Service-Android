package com.app.service.parking.di

import com.app.service.parking.feature.login.LoginViewModel
import com.app.service.parking.feature.main.MainViewModel
import com.app.service.parking.feature.main.review.ReviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

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
