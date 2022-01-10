package com.app.service.parking.presentation.review.all

import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityAllReviewBinding
import com.app.service.parking.databinding.ItemReviewTabBinding
import com.app.service.parking.presentation.base.BaseActivity
import com.app.service.parking.adapter.viewpager.ReviewViewPagerAdapter
import com.app.service.parking.model.dto.Lot
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllReviewActivity : BaseActivity<ActivityAllReviewBinding, AllReviewViewModel>() {

    override val layoutResId = R.layout.activity_all_review
    override val viewModel: AllReviewViewModel by viewModel()
    lateinit var viewPagerFragmentAdapter: ReviewViewPagerAdapter


    override fun initActivity() {
        setParkModel() // Intent로 받은 주차장 데이터로 초기화
        setBindingData() // 데이터 바인딩 설정
        initView() // 뷰 초기화
    }

    private fun initView() {
        with(binding) {

            // FragmentStateAdapter 생성
            viewPagerFragmentAdapter = ReviewViewPagerAdapter(this@AllReviewActivity)

            // 생성한 Adapter를 ViewPager와 연결
            reviewViewPager.adapter = viewPagerFragmentAdapter

            // TabLayout과 ViewPager2를 연결하고, TabItem의 메뉴명을 설정한다.
            val tabTitles = resources.getStringArray(R.array.review_types) // 탭 제목 리스트
            TabLayoutMediator(reviewTabLayout, reviewViewPager) { tab, position ->
                // 커스텀 탭 레이아웃 바인딩
                tab.customView = ItemReviewTabBinding.inflate(layoutInflater).run {
                    tabName = tabTitles[position] // 탭 제목 설정 (리뷰 타입)
                    reviewCount = viewModel?.getReviewCount(position).toString() // 리뷰 개수를 설정
                    root
                }
            }.attach()
        }
    }

    private fun setParkModel() {
        // Search Activity에서 검색 데이터로 받은 주차장 모델로 초기화
        viewModel.lotModel = intent.getSerializableExtra("model") as Lot
    }

    private fun setBindingData() {
        binding.viewModel = viewModel // ViewModel 바인딩
    }
}