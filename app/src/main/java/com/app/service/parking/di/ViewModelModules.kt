package com.app.service.parking.di

import com.app.service.parking.presentation.login.LoginViewModel
import com.app.service.parking.presentation.main.MainViewModel
import com.app.service.parking.presentation.review.all.AllReviewViewModel
import com.app.service.parking.presentation.review.main.ReviewViewModel
import com.app.service.parking.presentation.review.update.ReviewUpdateViewModel
import com.app.service.parking.presentation.review.write.ReviewWriteViewModel
import com.app.service.parking.presentation.search.SearchViewModel
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

val ReviewViewModelModule = module {
    viewModel {
        ReviewViewModel(get(), get())
    }
}

val ReviewWriteModelModule = module {
    viewModel {
        ReviewWriteViewModel(get())
    }
}

val ReviewUpdateViewModelModule = module {
    viewModel {
        ReviewUpdateViewModel(get())
    }
}


val AllReviewModelModule = module {
    viewModel {
        AllReviewViewModel(get())
    }
}

