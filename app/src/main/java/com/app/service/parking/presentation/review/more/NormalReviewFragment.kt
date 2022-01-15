package com.app.service.parking.presentation.review.more

import com.app.service.parking.R
import com.app.service.parking.adapter.recyclerview.MoreReviewRVAdapter
import com.app.service.parking.databinding.FragmentNormalReviewBinding
import com.app.service.parking.listener.RecyclerItemClickListener
import com.app.service.parking.presentation.base.BaseFragment
import com.app.service.parking.util.PopupImage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NormalReviewFragment : BaseFragment<FragmentNormalReviewBinding, MoreReviewViewModel>() {

    override val layoutResId: Int = R.layout.fragment_normal_review
    override val viewModel: MoreReviewViewModel by sharedViewModel()
    private var moreReviewRVAdapter: MoreReviewRVAdapter? = null

    override fun initActivity() {
        binding.viewModel = viewModel

        with(binding) {
            // '더 보기 리뷰' 리사이클러뷰 어댑터 초기화
            moreReviewRVAdapter = MoreReviewRVAdapter(object: RecyclerItemClickListener {
                // 리뷰를 클릭했을 때
                override fun onClick(position: Int, resId: Int?) {
                    // 리뷰 이미지 Uri : 보안상 문제로 https를 http로 문자 변환
                    val imgUri = viewModel?.normalReviewList?.value?.get(position)?.reviewImageUrl?.replace("https", "http")
                    // 이미지 팝업화면을 보여준다.
                    PopupImage().showImagePopup(requireContext(), imgUri)
                }
            })
            allReviewRecyclerView.adapter = moreReviewRVAdapter // 리사이클러뷰 어댑터 설정
            allReviewRecyclerView.layoutManager = MoreReviewRVAdapter.WrapContentLinearLayoutManager(requireContext())

            // 리뷰 데이터를 실시간 관찰
            viewModel?.normalReviewList?.observe(this@NormalReviewFragment) { reviewList ->
                // 리사이클러뷰 데이터 갱신
                moreReviewRVAdapter?.updateItems(reviewList)
            }

            // 서버로부터 리뷰 데이터 요청
            viewModel?.requestReviewList()
        }
    }
}