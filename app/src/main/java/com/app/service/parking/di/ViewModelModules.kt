package com.app.service.parking.di

import com.app.service.parking.presentation.view.login.LoginViewModel
import com.app.service.parking.presentation.view.main.MainViewModel
import com.app.service.parking.presentation.view.proposal.ProposalViewModel
import com.app.service.parking.presentation.view.review.more.MoreReviewViewModel
import com.app.service.parking.presentation.view.review.main.ReviewViewModel
import com.app.service.parking.presentation.view.review.update.ReviewUpdateViewModel
import com.app.service.parking.presentation.view.review.write.ReviewWriteViewModel
import com.app.service.parking.presentation.view.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel {
        LoginViewModel(get())
    }
}

val mainViewModelModule = module {
    viewModel {
        MainViewModel()
    }
}

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
}

val reviewViewModelModule = module {
    viewModel {
        ReviewViewModel(get(), get())
    }
}

val reviewWriteModelModule = module {
    viewModel {
        ReviewWriteViewModel(get())
    }
}

val reviewUpdateViewModelModule = module {
    viewModel {
        ReviewUpdateViewModel(get())
    }
}


val allReviewModelModule = module {
    viewModel {
        MoreReviewViewModel(get())
    }
}

val proposalModelModule = module {
    viewModel {
        ProposalViewModel()
    }
}

