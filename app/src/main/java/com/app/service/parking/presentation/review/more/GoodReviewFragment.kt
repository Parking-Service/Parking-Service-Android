package com.app.service.parking.presentation.review.more

import com.app.service.parking.R
import com.app.service.parking.databinding.FragmentGoodReviewBinding
import com.app.service.parking.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GoodReviewFragment : BaseFragment<FragmentGoodReviewBinding, MoreReviewViewModel>() {

    override val layoutResId: Int = R.layout.fragment_good_review
    override val viewModel: MoreReviewViewModel by sharedViewModel()

    override fun initActivity() {

    }
}