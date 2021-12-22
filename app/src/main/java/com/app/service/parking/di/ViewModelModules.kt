package com.app.service.parking.di

import com.app.service.parking.feature.login.LoginViewModel
import com.app.service.parking.feature.main.MainViewModel
import com.app.service.parking.feature.main.review.ReviewViewModel
import com.app.service.parking.feature.main.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel {
        LoginViewModel(get())
    }
}

val MainViewModelModule = module {
    viewModel {
        MainViewModel()
    }
}

val SearchViewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
}
