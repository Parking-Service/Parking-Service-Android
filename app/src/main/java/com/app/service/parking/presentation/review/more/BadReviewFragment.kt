package com.app.service.parking.presentation.review.more

import com.app.service.parking.R
import com.app.service.parking.databinding.FragmentBadReviewBinding
import com.app.service.parking.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BadReviewFragment : BaseFragment<FragmentBadReviewBinding, MoreReviewViewModel>() {

    override val layoutResId: Int = R.layout.fragment_bad_review
    override val viewModel: MoreReviewViewModel by sharedViewModel()

    override fun initActivity() {

    }
}