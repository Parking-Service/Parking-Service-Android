package com.app.service.parking.presentation.review.more

import com.app.service.parking.R
import com.app.service.parking.databinding.FragmentNormalReviewBinding
import com.app.service.parking.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NormalReviewFragment : BaseFragment<FragmentNormalReviewBinding, MoreReviewViewModel>() {

    override val layoutResId: Int = R.layout.fragment_normal_review
    override val viewModel: MoreReviewViewModel by sharedViewModel()

    override fun initActivity() {

    }
}